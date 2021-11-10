package com.pengxh.web.imagecollector.service.impl;

import com.alibaba.fastjson.JSON;
import com.pengxh.web.imagecollector.service.ISocketService;
import com.pengxh.web.imagecollector.uart.CommandManager;
import com.pengxh.web.imagecollector.uart.SerialPortManager;
import com.pengxh.web.imagecollector.utils.Constant;
import gnu.io.NRSerialPort;
import gnu.io.SerialPortEvent;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.TooManyListenersException;

/**
 * @author a203
 */
@Slf4j
@Service
public class SocketServiceImpl implements ISocketService {

    private NRSerialPort serialPort;

    public SocketServiceImpl() {
        //初始化串口
        Set<String> allPorts = NRSerialPort.getAvailableSerialPorts();
        if (!allPorts.isEmpty()) {
            //["/dev/tty.usbserial-1120","/dev/tty.wlan-debug"]
            if (allPorts.contains(Constant.USB_SERIAL)) {
                serialPort = new NRSerialPort(Constant.USB_SERIAL, Constant.BAUD_RATE);
                serialPort.connect();
                //登录指挥机
                byte[] loginCmd = CommandManager.createLoginCmd("^$CC0Z06,11,LOGIN,d, D$^");
                SerialPortManager.sendToPort(serialPort, loginCmd);
                try {
                    serialPort.addEventListener(serialPortEvent -> {
                        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                            String data = SerialPortManager.readFromPort(serialPort);
                            log.info("读取到数据===>" + data);
                        } else {
                            log.info("串口状态异常");
                            serialPort.removeEventListener();
                        }
                        serialPort.notifyOnDataAvailable(true);
                    });
                } catch (TooManyListenersException e) {
                    e.printStackTrace();
                }
            } else {
                log.info("无可用串口");
            }
        }
    }

    /**
     * Socket数据通信接口
     *
     * @param ctx 通道上下文
     * @param msg 读取到的数据
     */
    @Override
    public void communicate(ChannelHandlerContext ctx, Object msg) {
        byte[] data = (byte[]) msg;
        log.info("channelRead message ===> " + Arrays.toString(data));
        /**
         * 先解析再回应
         * */
        if (isCorrectData(data)) {
            int dataType = data[2];
            switch (dataType) {
                case 0x01:
                case 0x04:
                    /**
                     * 数据加密之后再与卫星通信
                     * */
                    String test = "*RN";
                    if (serialPort.isConnected()) {
                        SerialPortManager.sendToPort(serialPort, test.getBytes(StandardCharsets.UTF_8));
                    }
                    break;
                default:
                    break;
            }
            ctx.writeAndFlush(JSON.toJSONString("服务器已收到消息"));
        }
    }

    private boolean isCorrectData(byte[] bytes) {
        if (bytes[0] != Constant.BITS_OF_HEAD) {
            return false;
        }
        return bytes[bytes.length - 1] == Constant.BITS_OF_END;
    }
}

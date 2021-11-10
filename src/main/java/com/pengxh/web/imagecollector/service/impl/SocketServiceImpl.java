package com.pengxh.web.imagecollector.service.impl;

import com.pengxh.web.imagecollector.service.ISocketService;
import com.pengxh.web.imagecollector.uart.CommandManager;
import com.pengxh.web.imagecollector.uart.SerialPortManager;
import com.pengxh.web.imagecollector.utils.BytesUtil;
import com.pengxh.web.imagecollector.utils.Constant;
import gnu.io.NRSerialPort;
import gnu.io.SerialPortEvent;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            if (allPorts.contains(Constant.USB_CLIENT_SERIAL)) {
                serialPort = new NRSerialPort(Constant.USB_CLIENT_SERIAL, Constant.BAUD_RATE);
                serialPort.connect();
                //登录指挥机
                byte[] loginCmd = CommandManager.createLoginCmd("^$TT0Z06,11,LOGIN,d, D$^");
                SerialPortManager.sendToPort(serialPort, loginCmd);
                try {
                    serialPort.addEventListener(serialPortEvent -> {
                        // 解决数据断行
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                            byte[] data = SerialPortManager.readFromPort(serialPort);
                            log.info("收到数据===>" + Arrays.toString(data));
                            analyzeData(data);
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

    private void analyzeData(byte[] data) {
        if (-91 == data[0] && 90 == data[1]) {
            String hexString = BytesUtil.bytesToHexString(data);
            log.info("卫星通信返回值 ===> " + hexString);
        } else {
            String value = new String(data);
            if (value.startsWith("SN=")) {
                log.info("天通设备信息 ===> " + value);
            } else if (value.contains("GPS,V")) {
                log.info("天通北斗终端定位无信号，请找空旷位置再试");
            } else {
                log.info("其他指令返回值 ===> " + value);
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
        /**
         * 先解析再回应
         * */
        /**
         * 数据加密之后再与卫星通信
         * */
        if (serialPort.isConnected()) {
            SerialPortManager.sendToPort(serialPort, data);
        }
        ctx.writeAndFlush("服务器已收到消息");
    }
}

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
    private ChannelHandlerContext channelHandler;

    public SocketServiceImpl() {

    }

    @Override
    public void onSocketConnected(ChannelHandlerContext ctx) {
        this.channelHandler = ctx;
        //初始化串口
        Set<String> allPorts = NRSerialPort.getAvailableSerialPorts();
        if (!allPorts.isEmpty()) {
            boolean isClient;
            if (allPorts.contains(Constant.USB_CLIENT_SERIAL)) {
                isClient = true;
                serialPort = new NRSerialPort(Constant.USB_CLIENT_SERIAL, Constant.BAUD_RATE);
            } else if (allPorts.contains(Constant.USB_SERVER_SERIAL)) {
                isClient = false;
                serialPort = new NRSerialPort(Constant.USB_SERVER_SERIAL, Constant.BAUD_RATE);
            } else {
                isClient = false;
                log.info("无可用串口");
            }
            if (serialPort != null) {
                serialPort.connect();
                try {
                    serialPort.addEventListener(serialPortEvent -> {
                        // 解决数据断行
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                            analyzeData(SerialPortManager.readFromPort(serialPort));
                        } else {
                            log.info("串口状态异常");
                            serialPort.removeEventListener();
                        }
                        serialPort.notifyOnDataAvailable(true);
                    });
                } catch (TooManyListenersException e) {
                    e.printStackTrace();
                }
                if (isClient) {
                    SerialPortManager.setupSerialPortConfig(serialPort, SerialPortManager.CLIENT_CMD);
                } else {
                    SerialPortManager.setupSerialPortConfig(serialPort, SerialPortManager.SERVER_CMD);
                }
            }
        }
    }

    @Override
    public void onSocketDisconnect() {
        if (serialPort != null) {
            serialPort.disconnect();
        }
    }

    private void analyzeData(byte[] data) {
        log.info("串口收到数据 <=== " + Arrays.toString(data));
        if (-91 == data[0] && 90 == data[1]) {
            int[] unsignedData = BytesUtil.bytesToUnsigned(data);
            int type = unsignedData[3];
            switch (type) {
                case 0x01:
                    //读取北斗/GPS位置信息
                    sendToSocket("GPS,V");
                    break;
                case 0x02:
                    //数据接收
                    sendToSocket(new String(data));
                    break;
                case 0x03:
                    //数据发送成功
                    sendToSocket("SEND DATA SUCCESS!");
                    break;
                case 0x04:
                    //数据发送失败
                    sendToSocket("SEND DATA ERROR!");
                    break;
                case 0x05:
                    //当前天通信号强度

                    break;
                default:
                    break;
            }
        } else {
            log.info(new String(data));
        }
    }

    private void sendToSocket(String value) {
        if (channelHandler != null) {
            channelHandler.writeAndFlush(value);
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
        String decodeSMS = decodeSMS(data);
        /**
         * 先解析再回应
         * */
        byte[] bytes;
        if (decodeSMS.startsWith("*") || decodeSMS.startsWith("?")) {
            /**
             * 查看配置等直接通信即可
             * */
            bytes = decodeSMS.getBytes(StandardCharsets.UTF_8);
        } else {
            /**
             * 数据加密之后再与卫星通信
             * */
            bytes = CommandManager.createMessageCmd(decodeSMS);
        }
        if (serialPort != null && serialPort.isConnected()) {
            SerialPortManager.sendToPort(serialPort, bytes);
        }
    }

    /**
     * 此处仅为测试解析代码，需要根据实际情况写解析算法
     */
    public static String decodeSMS(byte[] bytes) {
        //帧头、帧长度、帧类型、子类型、机器人ID，CRC16校验码、帧尾总长度=8
        int dataLen = bytes.length - 8;

        byte[] dataBytes = new byte[dataLen];
        System.arraycopy(bytes, 5, dataBytes, 0, dataLen);
        StringBuilder builder = new StringBuilder();
        for (int dataByte : dataBytes) {
            builder.append((char) dataByte);
        }
        return builder.toString();
    }
}

package com.pengxh.web.imagecollector.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Calendar;

/**
 * 通信报文生成类
 *
 * @author a203
 */
@Slf4j
public class MessageHelper {

    private static final int MAX_PORT = 65535;
    private static final int MAX_HEART = 255;
    private static int serialNumber = 0;
    private static int heartNumber = 0;

    private static byte[] createCommonMsg(int length, byte version, byte type, byte extraCode,
                                          String serverHost, int serverPort, byte[] serialNumber) {
        byte[] msgBytes = new byte[length];
        /**
         * 前八位是报文标志
         * */
        for (int i = 0; i <= 7; i++) {
            msgBytes[i] = (byte) 0x8F;
        }
        /**
         * 数据包总长度
         * */
        byte[] lengthArray = new byte[2];
        lengthArray[1] = (byte) (length & 0xff);
        lengthArray[0] = (byte) (length >> 8 & 0xff);
        System.arraycopy(lengthArray, 0, msgBytes, 8, lengthArray.length);

        /**
         * 版本号
         * */
        msgBytes[10] = version;

        /**
         * 报文类型
         * */
        msgBytes[11] = type;

        /**
         * 附加代码
         * */
        msgBytes[12] = extraCode;

        /**
         * 年份，当前年份-2000
         * */
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR) - 2000;
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);

        msgBytes[13] = (byte) year;
        msgBytes[14] = (byte) month;
        msgBytes[15] = (byte) day;
        msgBytes[16] = (byte) hour;
        msgBytes[17] = (byte) minute;
        msgBytes[18] = (byte) second;

        byte[] hostArray = hostToByteArray(serverHost);
        System.arraycopy(hostArray, 0, msgBytes, 19, hostArray.length);

        /**
         * udp传输端口
         * */
        byte[] portArray = portToByteArray(serverPort);
        System.arraycopy(portArray, 0, msgBytes, 23, portArray.length);

        /**
         * 数据发送序列号
         * */
        System.arraycopy(serialNumber, 0, msgBytes, 25, serialNumber.length);

        /**
         * 保留内容
         * */
        byte[] retainArray = new byte[3];
        retainArray[1] = (byte) 0x00;
        retainArray[0] = (byte) 0x00;
        System.arraycopy(retainArray, 0, msgBytes, 29, retainArray.length);
        return msgBytes;
    }

    /**
     * 0-65535循环
     */
    public static byte[] createSerialNumber() {
        byte[] numberArray = new byte[2];
        numberArray[1] = (byte) (serialNumber & 0xff);
        numberArray[0] = (byte) (serialNumber >> 8 & 0xff);
        serialNumber++;
        if (serialNumber >= MAX_PORT) {
            serialNumber = 0;
        }
        return numberArray;
    }

    /**
     * 0-255循环
     */
    public static byte createHeartNumber() {
        byte heartByte = (byte) (heartNumber & 0xff);
        heartNumber++;
        if (heartNumber >= MAX_HEART) {
            heartNumber = 0;
        }
        return heartByte;
    }

    public static byte[] hostToByteArray(String host) {
        String[] split = host.split("\\.");
        byte[] hostArray = new byte[4];
        hostArray[0] = (byte) (Integer.parseInt(split[0]));
        hostArray[1] = (byte) (Integer.parseInt(split[1]));
        hostArray[2] = (byte) (Integer.parseInt(split[2]));
        hostArray[3] = (byte) (Integer.parseInt(split[3]));
        return hostArray;
    }

    public static byte[] portToByteArray(int port) {
        byte[] portArray = new byte[2];
        portArray[1] = (byte) (port & 0xff);
        portArray[0] = (byte) (port >> 8 & 0xff);
        return portArray;
    }

    /**
     * 客户端连接指令
     */
    public static byte[] createClientLoginMsg(String serverHost, int serverPort,
                                              String clientHost, int controlPort,
                                              int dataPort) {
        byte[] msgBytes = createCommonMsg(40, (byte) 0x00, (byte) 0x51, (byte) 0x01,
                serverHost, serverPort, createSerialNumber());

        /**
         * 校验和
         * */
        byte[] verifyArray = new byte[2];
        verifyArray[1] = (byte) 0x00;
        verifyArray[0] = (byte) 0x00;
        System.arraycopy(verifyArray, 0, msgBytes, 27, verifyArray.length);

        byte[] clientHostByteArray = hostToByteArray(clientHost);
        System.arraycopy(clientHostByteArray, 0, msgBytes, 32, clientHostByteArray.length);

        byte[] controlPortArray = portToByteArray(controlPort);
        System.arraycopy(controlPortArray, 0, msgBytes, 36, controlPortArray.length);

        byte[] dataPortArray = portToByteArray(dataPort);
        System.arraycopy(dataPortArray, 0, msgBytes, 38, dataPortArray.length);
        return msgBytes;
    }

    /**
     * 封装心跳包
     */
    public static byte[] createHeartBeatMsg(String serverHost, int serverPort) {
        byte[] msgBytes = createCommonMsg(32, (byte) 0x00, (byte) 0x60, createHeartNumber(),
                serverHost, serverPort, createSerialNumber());

        /**
         * 校验和
         * */
        byte[] verifyArray = new byte[2];
        verifyArray[1] = (byte) 0x00;
        verifyArray[0] = (byte) 0x00;
        System.arraycopy(verifyArray, 0, msgBytes, 27, verifyArray.length);
        return msgBytes;
    }
}

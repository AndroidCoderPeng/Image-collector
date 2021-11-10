package com.pengxh.web.imagecollector.uart;

import gnu.io.NRSerialPort;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author a203
 */
@Slf4j
public class SerialPortManager {
    /**
     * 往串口发送数据
     *
     * @param serialPort 串口对象
     * @param order      待发送数据
     */
    public static void sendToPort(NRSerialPort serialPort, byte[] order) {
        log.info("串口发送数据 ===> " + Arrays.toString(order));
        DataOutputStream outs = null;
        try {
            outs = new DataOutputStream(serialPort.getOutputStream());
            outs.write(order);
            outs.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outs != null) {
                    outs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 串口读取数据
     *
     * @param serialPort 串口对象
     */
    public static byte[] readFromPort(NRSerialPort serialPort) {
        InputStream inputStream = null;
        byte[] buffer = {};
        try {
            inputStream = serialPort.getInputStream();
            //获得数据长度
            int sum = inputStream.available();
            while (sum != 0) {
                //如果缓冲区读取结束，且数组长度为7（防止串口数据发送缓慢读取到空数据），跳出循环
                if (inputStream.available() > 0) {
                    buffer = new byte[sum];
                    inputStream.read(buffer);
                    sum = inputStream.available();
                }
            }
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }
}


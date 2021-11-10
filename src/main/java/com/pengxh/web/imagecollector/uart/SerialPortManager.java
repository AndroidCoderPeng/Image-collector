package com.pengxh.web.imagecollector.uart;

import gnu.io.NRSerialPort;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
        log.info("往串口发送数据===>" + Arrays.toString(order));
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
    public static String readFromPort(NRSerialPort serialPort) {
        DataInputStream inputStream = new DataInputStream(serialPort.getInputStream());
        String data = "";
        try {
            while (inputStream.available() > 0) {
                char read = (char) inputStream.read();
                data = data.concat(Character.toString(read));
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}


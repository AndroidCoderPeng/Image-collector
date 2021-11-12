package com.pengxh.web.imagecollector.uart;

import gnu.io.NRSerialPort;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author a203
 */
@Slf4j
public class SerialPortManager {
    /**
     * 客户端参数设置指令
     */
    public static final List<byte[]> CLIENT_CMD = Arrays.asList(
            /*设置天通关闭Debug模式*/
            CommandManager.CMD_CLOSE_DEBUG.getBytes(StandardCharsets.UTF_8),
            /*设置天通连续快速传输模式*/
            CommandManager.CMD_DTU_MODE.getBytes(StandardCharsets.UTF_8),
            /*设置天通终端天线工作模式*/
            CommandManager.CMD_WORK_MODE.getBytes(StandardCharsets.UTF_8),
            /*设置短信发送目的地的号码数量*/
            CommandManager.createTargetNumberCmd(new String[]{"8617400542542", "8618765997865"}),
            /*设置短信中心号码SCA*/
            CommandManager.CMD_CENTER_NUMBER.getBytes(StandardCharsets.UTF_8),
            /*设置终端号码*/
            CommandManager.CMD_CLIENT_NUMBER.getBytes(StandardCharsets.UTF_8)
    );

    /**
     * 指挥机参数设置指令
     */
    public static final List<byte[]> SERVER_CMD = Arrays.asList(
            /*设置天通关闭Debug模式*/
            CommandManager.CMD_CLOSE_DEBUG.getBytes(StandardCharsets.UTF_8),
            /*设置天通连续快速传输模式*/
            CommandManager.CMD_DTU_MODE.getBytes(StandardCharsets.UTF_8),
            /*设置天通指挥机天线工作模式*/
            CommandManager.CMD_WORK_MODE.getBytes(StandardCharsets.UTF_8),
            /*设置短信发送目的地的号码数量*/
            CommandManager.createTargetNumberCmd(new String[]{"8617400542542", "8618765997865"}),
            /*设置短信中心号码SCA*/
            CommandManager.CMD_CENTER_NUMBER.getBytes(StandardCharsets.UTF_8),
            /*设置指挥机号码*/
            CommandManager.CMD_SERVER_NUMBER.getBytes(StandardCharsets.UTF_8)
    );

    public static void setupSerialPortConfig(NRSerialPort serialPort, List<byte[]> cmd) {
        int index = 0;
        while (index < cmd.size()) {
            SerialPortManager.sendToPort(serialPort, cmd.get(index));
            try {
                index++;
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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


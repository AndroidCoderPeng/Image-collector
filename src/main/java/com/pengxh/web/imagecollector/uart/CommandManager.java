package com.pengxh.web.imagecollector.uart;

import com.pengxh.web.imagecollector.utils.BytesUtil;
import com.pengxh.web.imagecollector.utils.TEA;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author a203
 */
@Slf4j
public class CommandManager {
    public static final String CMD_CLOSE_DEBUG = "*XF,DEBUG=0";
    public static final String CMD_DTU_MODE = "*XF,MODE=1,#";
    public static final String CMD_WORK_MODE = "*W:DEV:S";
    public static final String CMD_CENTER_NUMBER = "*W:SCA:8617400010200";
    public static final String CMD_CLIENT_NUMBER = "*W:SN:TT0Z07";
    public static final String CMD_SERVER_NUMBER = "*W:SN:C";

    /**
     * 指挥机登录指令
     * *AT^IOTDATA=24,"^$CC0Z06,11,LOGIN,d, D$^"
     */
    public static byte[] createServerLoginCmd(String serverName, String pwd) {
        int dataLength = 2 + serverName.length() + 1 + pwd.length() + 13;
        String cmd = "*AT^IOTDATA=" + dataLength + ",\"^$" + serverName + "," + pwd + ",LOGIN,d, D$^\"";
        return cmd.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 设置短信发送目的地的号码数量
     */
    public static byte[] createTargetNumberCmd(String[] numbers) {
        //*W:TPA:2,8617419290107,861771542345
        StringBuilder cmd = new StringBuilder("*W:TPA:" + numbers.length + ",");
        for (int i = 0; i < numbers.length; i++) {
            if (i != numbers.length - 1) {
                cmd.append(numbers[i]).append(",");
            } else {
                cmd.append(numbers[i]);
            }
        }
        return cmd.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 客户端发送指令
     * &M# 天通北斗终端将视M为数据内容，将数据内容通过DTU发送给远端
     */
    public static byte[] createClientMessageCmd(String data) {
        //数据转byte数组
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        //将源数据加密
        byte[] encrypt = TEA.encrypt(bytes);
        //加密之后的数据转16进制
        String dataHex = BytesUtil.bytesToHexString(encrypt);
        //将16进制密文和串口协议拼接然后再转byte数组发送出去
        String cmd = "&" + dataHex + "#";
        return cmd.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 指挥机发送指令
     * *AT^IOTDATA=31,"^$CC0Z06,11,SEND,TT0Z07, 1234$^"
     */
    public static byte[] createServerMessageCmd(String data, String serverAccount, String pwd, String clientAccount) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = TEA.encrypt(bytes);
        String dataHex = BytesUtil.bytesToHexString(encrypt);
        int dataLength = 2 + serverAccount.length() + 1 + pwd.length() + 6 + clientAccount.length() + 2 + dataHex.length() + 2;
        String cmd = "*AT^IOTDATA="
                + dataLength
                + ",\"^$" + serverAccount + ","
                + pwd + ",SEND,"
                + clientAccount + ", "
                + dataHex
                + "$^\"";
        log.info(cmd);
        return cmd.getBytes(StandardCharsets.UTF_8);
    }
}

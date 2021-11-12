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
    public static final String CMD_SERVER_NUMBER = "*W:SN:CC0Z06";

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
     * &M# 天通北斗终端将视M为数据内容，将数据内容通过SMS或DTU发送给远端
     * 发送成功通过串口返回SENDDATA,OK（帧格式） ，发送未成功返回SENDDATA,ERR（帧格式）
     */
    public static byte[] createMessageCmd(String data) {
        byte[] smsBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = TEA.encrypt(smsBytes);
        StringBuilder builder = new StringBuilder();
        for (int b : BytesUtil.bytesToUnsigned(encrypt)) {
            builder.append(Integer.toString(b, 16));
        }
        String cmd = "&" + builder + "#";
        log.info(cmd);
        return cmd.getBytes(StandardCharsets.UTF_8);
    }
}

package com.pengxh.web.imagecollector.uart;

import java.nio.charset.StandardCharsets;

/**
 * 指令拼接
 *
 * @author a203
 */
public class CommandManager {
    public static final String CMD_WORK_MODE = "*W:DEV:S";
    public static final String CMD_CENTER_NUMBER = "*W:SCA:8617400010200";

    private static final String CMD_HEADER = "*AT^IOTDATA=";

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
     * *AT^IOTDATA=21,"^$CC1,11,LOGIN,d, D$^"
     * <p>
     * 21是双引号中间的数据长度，CC1指挥中心名称，11是指挥中心密码，d+逗号+空格+D
     * <p>
     * 登录指令和发送数据指令之间，需要间隔500ms以上
     */
    public static byte[] createLoginCmd(String data) {
        String cmd = CMD_HEADER + data.length() + "," + "\"" + data + "\"";
        return cmd.getBytes(StandardCharsets.UTF_8);
    }
}


package com.pengxh.web.imagecollector.uart;

import java.nio.charset.StandardCharsets;

/**
 * 指令拼接
 *
 * @author a203
 */
public class CommandManager {
    private static final String CMD_HEADER = "*AT^IOTDATA=";

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


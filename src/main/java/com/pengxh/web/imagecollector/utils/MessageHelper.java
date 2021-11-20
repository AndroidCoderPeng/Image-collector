package com.pengxh.web.imagecollector.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 通信报文生成类
 *
 * @author a203
 */
@Slf4j
public class MessageHelper {
    /**
     * 封装心跳包
     */
    public static byte[] createHeartBeatMsg() {
        byte[] msgBytes = new byte[32];

        /**
         * 前八位是报文标志
         * */
        for (int i = 0; i < 8; i++) {
            msgBytes[i] = (byte) 0x8F;
        }

        /**
         * 数据包总长度
         * */
        int length = msgBytes.length;
        byte[] bytes = Integer.toHexString(length).getBytes(StandardCharsets.UTF_8);


//        /**
//         * 版本号
//         * */
//        msgBytes[10] = (byte) 0x00;
//
//        /**
//         * 报文类型
//         * */
//        msgBytes[11] = (byte) 0x60;
//        /**
//         * 附加代码
//         * */
//        msgBytes[12] = (byte) 0x60;

        log.info(Arrays.toString(msgBytes));
        return msgBytes;
    }
}

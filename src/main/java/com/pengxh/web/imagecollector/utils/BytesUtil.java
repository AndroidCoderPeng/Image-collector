package com.pengxh.web.imagecollector.utils;

import java.util.Locale;

/**
 * @author a203
 */
public class BytesUtil {
    public static String bytesToHexString(byte[] data) {
        int[] unsignedBytes = bytesToUnsigned(data);
        StringBuilder builder = new StringBuilder();
        for (int datum : unsignedBytes) {
            String hex = Integer.toHexString(datum);
            if (hex.length() < 2) {
                builder.append(0);
            }
            builder.append(hex);
        }
        return builder.toString().toUpperCase(Locale.ROOT);
    }

    /**
     * 将负值byte补位为无符号byte
     * C/C++/C#都有无符号数据类型，Java需要补位转换
     */
    public static int[] bytesToUnsigned(byte[] data) {
        int[] array = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            byte datum = data[i];
            int temp;
            if (datum < 0) {
                temp = 0xFF & datum;
            } else {
                temp = datum;
            }
            array[i] = temp;
        }
        return array;
    }
}

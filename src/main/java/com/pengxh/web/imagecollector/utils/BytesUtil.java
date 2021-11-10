package com.pengxh.web.imagecollector.utils;

import java.util.Locale;

/**
 * @author a203
 */
public class BytesUtil {
    public static String bytesToHexString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte datum : data) {
            int temp;
            if (datum < 0) {
                temp = 0xFF & datum;
            } else {
                temp = datum;
            }
            builder.append(Integer.toString(temp, 16).toUpperCase(Locale.ROOT));
        }
        return builder.toString();
    }
}

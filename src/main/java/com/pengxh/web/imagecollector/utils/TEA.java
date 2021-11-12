package com.pengxh.web.imagecollector.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * TEA加解密
 *
 * @author a203
 */
@Slf4j
public class TEA {

    private static final int EIGENVALUES = 0x9E3779B9;

    public static final long[] KEY = {
            (long) 0x789F5645, (long) 0xF68BD5A4,
            (long) 0x81963FFA, (long) 0x458FAC58
    };

    public static byte[] encrypt(byte[] contentBytes) {
        long k0 = KEY[0], k1 = KEY[1], k2 = KEY[2], k3 = KEY[3];
        byte left = contentBytes[0], right = contentBytes[1];
        //加密次数
        int times = 32;
        int sum = 0;
        while (times-- > 0) {
            sum += EIGENVALUES;
            left += ((right << 4) + k0) ^ (right + sum) ^ ((right >> 5) + k1);
            right += ((left << 4) + k2) ^ (left + sum) ^ ((left >> 5) + k3);
        }
        contentBytes[0] = left;
        contentBytes[1] = right;
        return contentBytes;
    }

    public static byte[] decrypt(byte[] encryptBytes) {
        byte y = encryptBytes[0], z = encryptBytes[1];
        int sum = 0xC6EF3720, i;
        long k0 = KEY[0], k1 = KEY[1], k2 = KEY[2], k3 = KEY[3];
        for (i = 0; i < 32; i++) {
            z -= ((y << 4) + k2) ^ (y + sum) ^ ((y >> 5) + k3);
            y -= ((z << 4) + k0) ^ (z + sum) ^ ((z >> 5) + k1);
            sum -= EIGENVALUES;
        }
        encryptBytes[0] = y;
        encryptBytes[1] = z;
        return encryptBytes;
    }
}

package com.pengxh.web.imagecollector.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * TEA加解密
 *
 * @author a203
 */
@Slf4j
public class TEA {

    private static final int EIGENVALUES = 0x9E3779B9;

    public static byte[] encrypt(String content, long[] key) {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        long k0 = key[0], k1 = key[1], k2 = key[2], k3 = key[3];
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

    public static byte[] decrypt(byte[] v, long[] key) {
        byte y = v[0], z = v[1];
        int sum = 0xC6EF3720, i;
        long k0 = key[0], k1 = key[1], k2 = key[2], k3 = key[3];
        for (i = 0; i < 32; i++) {
            z -= ((y << 4) + k2) ^ (y + sum) ^ ((y >> 5) + k3);
            y -= ((z << 4) + k0) ^ (z + sum) ^ ((z >> 5) + k1);
            sum -= EIGENVALUES;
        }
        v[0] = y;
        v[1] = z;
        return v;
    }
}

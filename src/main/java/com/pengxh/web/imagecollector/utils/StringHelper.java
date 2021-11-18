package com.pengxh.web.imagecollector.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author a203
 */
@Slf4j
public class StringHelper {
    public static String[] formatTargetNumber(String target) {
        if (!target.contains(",")) {
            return new String[]{target};
        }
        String[] targetNumber = target.split(",");
        log.info("targetNumber ===> " + Arrays.toString(targetNumber));
        return targetNumber;
    }
}

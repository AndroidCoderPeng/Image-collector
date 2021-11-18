package com.pengxh.web.imagecollector.dto;

import lombok.Data;

/**
 * @author a203
 */
@Data
public class MemoryUsageDTO {

    private Memory memory;
    private Swap swap;

    @Data
    public static class Memory {
        private String total;
        private String used;
        private String free;
        private String shared;
        private String cache;
        private String available;
    }

    @Data
    public static class Swap {
        private String total;
        private String used;
        private String free;
    }
}

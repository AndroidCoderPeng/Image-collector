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
        private Long total;
        private Long used;
        private Long free;
        private Long shared;
        private Long cache;
        private Long available;
    }

    @Data
    public static class Swap {
        private Long total;
        private Long used;
        private Long free;
    }
}

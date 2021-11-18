package com.pengxh.web.imagecollector.dto;

import lombok.Data;

/**
 * @author a203
 */
@Data
public class CpuUsageDTO {
    /**
     * 用户态使用的cpu时间比
     */
    private String userRatio;
    /**
     * 系统态使用的cpu时间比
     */
    private String systemRatio;
    /**
     * 用做nice加权的进程分配的用户态cpu时间比
     */
    private String niceRatio;
    /**
     * 空闲的cpu时间比
     */
    private String freeRatio;
}

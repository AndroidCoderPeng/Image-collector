package com.pengxh.web.imagecollector.dto;

import lombok.Data;

/**
 * @author a203
 */
@Data
public class CpuUsageDTO {
    /**
     * 任务进程数
     */
    private Integer taskCount;
    /**
     * 用户态使用的cpu时间比
     */
    private Float userRatio;
    /**
     * 系统态使用的cpu时间比
     */
    private Float systemRatio;
    /**
     * 用做nice加权的进程分配的用户态cpu时间比
     */
    private Float niceRatio;
    /**
     * 空闲的cpu时间比
     */
    private Float freeRatio;
}

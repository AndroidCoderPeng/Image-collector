package com.pengxh.web.imagecollector.dto;

import lombok.Data;

/**
 * @author a203
 */
@Data
public class RunningJavaDTO {
    private String owner;
    private String pid;
    private String cpu;
    private String memory;
    private String vtMemory;
    private String pyMemory;
    private String status;
    private String startDate;
    private String totalTime;
    private String processName;
}

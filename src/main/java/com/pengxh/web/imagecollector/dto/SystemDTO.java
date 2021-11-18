package com.pengxh.web.imagecollector.dto;

import lombok.Data;

import java.util.List;

/**
 * @author a203
 */
@Data
public class SystemDTO {
    private MemoryUsageDTO memoryUsage;
    private List<StorageUsageDTO> storageUsage;
    private CpuUsageDTO cpuUsage;
    private List<RunningJavaDTO> javaProcess;
}

package com.pengxh.web.imagecollector.dto;

import lombok.Data;

/**
 * @author a203
 */
@Data
public class StorageUsageDTO {
    private String systemName;
    private String total;
    private String used;
    private String available;
    private String usage;
    private String path;
}

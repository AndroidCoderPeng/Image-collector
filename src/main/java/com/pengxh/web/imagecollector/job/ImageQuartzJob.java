package com.pengxh.web.imagecollector.job;

import com.pengxh.web.imagecollector.service.IImageService;
import com.pengxh.web.imagecollector.service.ISystemService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务，每10s执行一次
 *
 * @author a203
 */
@Component
public class ImageQuartzJob {

    private final IImageService imageService;
    private final ISystemService systemService;

    public ImageQuartzJob(IImageService imageService, ISystemService systemService) {
        this.imageService = imageService;
        this.systemService = systemService;
    }

    @Scheduled(cron = "0/10 * *  * * ? ")
    public void execute() {
//        imageService.saveImage();
//        systemService.obtainLinuxServerInfo();
    }
}

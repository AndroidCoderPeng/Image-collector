package com.pengxh.web.imagecollector.job;

import com.pengxh.web.imagecollector.service.IImageService;
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

    public ImageQuartzJob(IImageService imageService) {
        this.imageService = imageService;
    }

    @Scheduled(cron = "0/10 * *  * * ? ")
    public void execute() {
        imageService.saveImage();
    }
}

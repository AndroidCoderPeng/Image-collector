package com.pengxh.web.imagecollector.job;

import com.pengxh.web.imagecollector.socket.udp.BootNettyUdpClient;
import com.pengxh.web.imagecollector.utils.MessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author a203
 */
@Component
public class UdpKeepLiveJob {

    @Resource
    private BootNettyUdpClient udpClient;

    @Scheduled(cron = "0/10 * *  * * ? ")
    public void execute() {
        udpClient.sendDataPacket(MessageHelper.createHeartBeatMsg());
    }
}

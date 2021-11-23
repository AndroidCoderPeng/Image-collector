package com.pengxh.web.imagecollector.job;

import com.pengxh.web.imagecollector.socket.udp.BootNettyUdpClient;
import com.pengxh.web.imagecollector.utils.MessageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author a203
 */
@Component
public class UdpKeepLiveJob {

    @Value("${socket.udp.serverHost}")
    private String serverHost;

    @Value("${socket.udp.serverPort}")
    private Integer serverPort;

    @Resource
    private BootNettyUdpClient udpClient;

    @Scheduled(cron = "0/10 * *  * * ? ")
    public void execute() {
        udpClient.sendDataPacket(MessageHelper.createHeartBeatMsg(serverHost, serverPort));
    }
}

package com.pengxh.web.imagecollector.socket.udp;

import com.pengxh.web.imagecollector.service.ISocketService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author a203
 */
@Slf4j
@Component
public class BootNettyUdpServer implements CommandLineRunner {

    @Value("${socket.udp.port}")
    private Integer port;

    private final ISocketService socketService;

    public BootNettyUdpServer(ISocketService socketService) {
        this.socketService = socketService;
    }

    @Async
    @Override
    public void run(String... args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new NettyUdpServerHandler(socketService));
            /**
             * 绑定端口，同步等待成功
             */
            Channel channel = bootstrap.bind(port).sync().channel();
            log.info(this.getClass().getSimpleName() + " has been started, and Port:" + port + " has been occupied....");
            /**
             * 等待服务器监听端口关闭
             */
            channel.closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            /**
             * 退出，释放线程池资源
             */
            bossGroup.shutdownGracefully();
        }
    }
}

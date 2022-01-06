package com.pengxh.web.imagecollector.socket.udp;

import com.pengxh.web.imagecollector.service.ISocketService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
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
            /**
             * ServerBootstrap 是一个启动NIO服务的辅助启动类
             */
            Bootstrap serverBootstrap = new Bootstrap();
            /**
             * 设置group，将bossGroup，workerGroup线程组传递到ServerBootstrap
             */
            serverBootstrap.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 100)
                    /**
                     * 设置 I/O处理类,主要用于网络I/O事件，记录日志，编码、解码消息
                     */
                    .handler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            /**
                             * 自定义ChannelInboundHandlerAdapter
                             */
                            pipeline.addLast(new NettyUdpServerHandler(socketService));
                        }
                    });
            /**
             * 绑定端口，同步等待成功
             */
            Channel channel = serverBootstrap.bind(port).sync().channel();
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

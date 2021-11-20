package com.pengxh.web.imagecollector.socket.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class BootNettyUdpServer {

    @Value("${socket.udp.port}")
    private Integer port;

    public void bind() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            /**
             * UDP方式使用Bootstrap
             */
            Bootstrap serverBootstrap = new Bootstrap();
            /**
             * 设置group，将bossGroup，workerGroup线程组传递到ServerBootstrap
             */
            serverBootstrap.group(eventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext context, DatagramPacket dataPacket) throws Exception {
                            String data = dataPacket.content().toString(CharsetUtil.UTF_8);
                            log.info("UDP收到消息 <=== " + data);

                            context.writeAndFlush(new DatagramPacket(
                                    Unpooled.copiedBuffer(System.currentTimeMillis() / 1000 + "", CharsetUtil.UTF_8),
                                    dataPacket.sender()
                            ));
                        }
                    });
            log.info("Socket port has open, and Port:" + port + " has been occupied....");
            /**
             * 绑定端口，同步等待成功
             */
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            /**
             * 等待服务器监听端口关闭
             */
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            /**
             * 退出，释放线程池资源
             */
            eventLoopGroup.shutdownGracefully();
        }
    }
}

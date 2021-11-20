package com.pengxh.web.imagecollector.socket.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * @author a203
 */
@Slf4j
@Service
public class BootNettyUdpClient {

    @Value("${socket.udp.host}")
    private String host;

    @Value("${socket.udp.port}")
    private Integer port;

    private Channel channel;

    public void bind() {
        log.info("BootNettyUdpClient Start");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ClientHandlerAdapter());
            channel = clientBootstrap.bind(0).sync().channel();

            sendDataPacket();

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendDataPacket() {
        if (channel != null && channel.isActive()) {
            log.info("sendDataPacket");
            ByteBuf byteBuf = Unpooled.copiedBuffer("I am UDP Client", CharsetUtil.UTF_8);
            DatagramPacket datagramPacket = new DatagramPacket(byteBuf, new InetSocketAddress(host, port));
            channel.writeAndFlush(datagramPacket);
        }
    }
}

package com.pengxh.web.imagecollector.socket.udp;

import com.pengxh.web.imagecollector.utils.MessageHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @author a203
 */
@Slf4j
@Component
public class BootNettyUdpClient implements CommandLineRunner {

    @Value("${socket.udp.host}")
    private String host;

    @Value("${socket.udp.port}")
    private Integer port;

    private Channel channel;
    /**
     * 是否可以发送心跳包
     */
    private boolean startHeartBeat = false;

    @Async
    @Override
    public void run(String... args) {
        log.info("BootNettyUdpClient Start");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext context, DatagramPacket datagramPacket) throws Exception {
                            analyzeDatagramPacket(datagramPacket);
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                            cause.printStackTrace();
                            ctx.close();
                        }
                    });
            channel = clientBootstrap.bind(0).sync().channel();

            byte[] loginMsg = MessageHelper.createClientLoginMsg("111.198.10.15", 12210,
                    "192.168.43.66", 53460, 53460);
            ByteBuf byteBuf = Unpooled.copiedBuffer(loginMsg);
            DatagramPacket datagramPacket = new DatagramPacket(byteBuf, new InetSocketAddress(host, port));
            channel.writeAndFlush(datagramPacket);
            log.info("UDP登录指令 ===> " + Arrays.toString(loginMsg));

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 解析UDP返回的数据
     */
    private void analyzeDatagramPacket(DatagramPacket datagramPacket) {
        ByteBuf byteBuf = datagramPacket.content();
        byte[] encoded = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(encoded);
        log.info("收到UDP服务器数据 <=== " + Arrays.toString(encoded));
        if (encoded[11] == 50) {
            startHeartBeat = true;
        } else if (encoded[11] == 51) {
            /**
             * 解析数据
             * */

        } else {
            log.info("code ===> " + encoded[11]);
        }
    }

    public void sendDataPacket(byte[] message) {
        if (channel != null && channel.isActive()) {
            if (startHeartBeat) {
                ByteBuf byteBuf = Unpooled.copiedBuffer(message);
                DatagramPacket datagramPacket = new DatagramPacket(byteBuf, new InetSocketAddress(host, port));
                channel.writeAndFlush(datagramPacket);
                log.info("UDP心跳指令 ===> " + Arrays.toString(message));
            }
        }
    }
}

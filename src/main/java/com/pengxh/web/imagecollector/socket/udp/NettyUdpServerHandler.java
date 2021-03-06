package com.pengxh.web.imagecollector.socket.udp;

import com.pengxh.web.imagecollector.service.ISocketService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author a203
 */
@Slf4j
public class NettyUdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private final ISocketService socketService;

    public NettyUdpServerHandler(ISocketService socketService) {
        this.socketService = socketService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        ByteBuf byteBuf = datagramPacket.content();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        log.info("channelRead0 ===> " + Arrays.toString(bytes));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}

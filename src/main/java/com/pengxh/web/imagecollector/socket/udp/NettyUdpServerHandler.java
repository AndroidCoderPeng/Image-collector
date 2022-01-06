package com.pengxh.web.imagecollector.socket.udp;

import com.pengxh.web.imagecollector.service.ISocketService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

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
        String msg = datagramPacket.content().toString(CharsetUtil.UTF_8);
        log.info("channelRead0 ===> " + msg);
    }
}

package com.pengxh.web.imagecollector.socket;

import com.pengxh.web.imagecollector.service.ISocketService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Administrator
 */
@Slf4j
public class ChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final ISocketService socketService;

    public ChannelHandlerAdapter(ISocketService socketService) {
        this.socketService = socketService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        socketService.communicate(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();//抛出异常，断开与客户端的连接
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().read();
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = socketAddress.getAddress().getHostAddress();
        socketService.onSocketConnected();
        log.info("channelActive ===> " + ip);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = socketAddress.getAddress().getHostAddress();
        socketService.onSocketDisconnect();
        log.info("channelInactive ===> " + ip);
        ctx.close(); //断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = socketAddress.getAddress().getHostAddress();
        log.info("userEventTriggered ===> " + ip);
        ctx.close();//超时时断开连接
    }
}

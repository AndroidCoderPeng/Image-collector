package com.pengxh.web.imagecollector.socket;

import io.netty.channel.Channel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author Administrator
 */
public class ChannelInitializer<SocketChannel> extends io.netty.channel.ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) {
        // ChannelOutboundHandler，依照逆序执行
        channel.pipeline().addLast("encoder", new StringEncoder());

        // 属于ChannelInboundHandler，依照顺序执行
        channel.pipeline().addLast("decoder", new StringDecoder());
        /**
         * 自定义ChannelInboundHandlerAdapter
         */
        channel.pipeline().addLast(new ChannelHandlerAdapter());
    }
}

package com.pengxh.web.imagecollector.service;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author a203
 */
public interface ISocketService {
    /**
     * Socket数据通信接口
     *
     * @param ctx 通道上下文
     * @param msg 读取到的数据
     */
    void communicate(ChannelHandlerContext ctx, Object msg);
}

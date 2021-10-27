package com.pengxh.web.imagecollector.service.impl;

import com.alibaba.fastjson.JSON;
import com.pengxh.web.imagecollector.service.ISocketService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author a203
 */
@Slf4j
@Service
public class SocketServiceImpl implements ISocketService {

    public SocketServiceImpl() {

    }

    /**
     * Socket数据通信接口
     *
     * @param ctx 通道上下文
     * @param msg 读取到的数据
     */
    @Override
    public void communicate(ChannelHandlerContext ctx, Object msg) {
        byte[] data = (byte[]) msg;
        log.info("channelRead message ===> " + Arrays.toString(data));
        /**
         * 先解析再回应
         * */
        ctx.writeAndFlush(JSON.toJSONString("服务器已收到消息"));
    }
}

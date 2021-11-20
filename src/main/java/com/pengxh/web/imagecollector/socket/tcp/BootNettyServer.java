package com.pengxh.web.imagecollector.socket.tcp;

import com.pengxh.web.imagecollector.service.ISocketService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class BootNettyServer {

    @Value("${socket.port}")
    private Integer port;

    private final ISocketService socketService;

    public BootNettyServer(ISocketService socketService) {
        this.socketService = socketService;
    }

    public void bind() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /**
             * ServerBootstrap 是一个启动NIO服务的辅助启动类
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /**
             * 设置group，将bossGroup，workerGroup线程组传递到ServerBootstrap
             */
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //连接数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //缓冲大小，initial要介于minimum和maximum之间
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(512, 1024, 2048))
                    /**
                     * 设置 I/O处理类,主要用于网络I/O事件，记录日志，编码、解码消息
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //接收消息格式-byte[]
                            pipeline.addLast("decoder", new ByteArrayDecoder());
                            //发送消息格式-String
                            pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            /**
                             * 自定义ChannelInboundHandlerAdapter
                             */
                            pipeline.addLast(new ChannelHandlerAdapter(socketService));
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
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

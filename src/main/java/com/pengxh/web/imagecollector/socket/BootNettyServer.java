package com.pengxh.web.imagecollector.socket;

import com.pengxh.web.imagecollector.service.ISocketService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 */
@Slf4j
public class BootNettyServer {

    private final ISocketService socketService;

    public BootNettyServer(ISocketService socketService) {
        this.socketService = socketService;
    }

    public void bind(int port) {
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
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            //连接数
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            //长连接
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //缓冲大小，initial要介于minimum和maximum之间
            serverBootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(512, 1024, 2048));
            /**
             * 设置 I/O处理类,主要用于网络I/O事件，记录日志，编码、解码消息
             */
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

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
            log.info("端口已开启，占用" + port + "端口号....");
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

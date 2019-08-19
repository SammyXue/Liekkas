package com.xcm.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;



public class MyNettyServer {
    private static final String IP = "localhost";
    private static final int PORT = 5656;
    private static final int BIZGROUPSIZE = Runtime.getRuntime()
            .availableProcessors() * 2;
    private static final int BIZTHREADSIZE =100;
    private static final int MAX_FRAME_LENGTH = 10240;

    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(
            BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(
            BIZTHREADSIZE);



    public static void service() throws Exception {

        System.out.println("开始启动服务器...");

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {

                ChannelPipeline pipeline = ch.pipeline();
//                pipeline.addLast(new IdleStateHandler(120, 40, 0,
//                        TimeUnit.SECONDS));
//                // decoded
//                pipeline.addLast(new LengthFieldBasedFrameDecoder(
//                        MAX_FRAME_LENGTH, 0, 4, 0, 4));
//                // encoded
//                pipeline.addLast(new LengthFieldPrepender(4));
//                pipeline.addLast(new ProtobufEncoder());

                // 注册handler
                pipeline.addLast(new Decoder());
                pipeline.addLast(new Encoder());

                pipeline.addLast(new ServerHandler());
            }

        });
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
         bootstrap.bind(IP, PORT).sync();
        System.out.println("服务器已启动");
        System.out.println("Binding port: " + PORT);
//        f.channel().closeFuture().sync();
    }

    protected static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("开始关闭netty服务器...");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }));

    }

    public void init() throws Exception {
        System.out.println("init------------");
        MyNettyServer.service();
    }

    public static void main(String[] args) throws Exception {
        MyNettyServer.service();
        MyNettyServer.addShutdownHook();
    }
}

package com.liekkas.core.netty;

import com.liekkas.core.config.NettyServerConfig;
import com.liekkas.core.proto.Protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.apache.log4j.Logger;


public class NettyServer {

    private static Logger logger = Logger.getLogger(NettyServer.class);
    private static final int MAX_FRAME_LENGTH = 10240;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private NettyServerConfig config;

    public NettyServer(NettyServerConfig config) {
        this.bossGroup = new NioEventLoopGroup(
                config.getBossGroupSize());
        this.workerGroup = new NioEventLoopGroup(
                config.getWorkerGroupSize());
        this.config = config;
    }

    public void start() throws Exception {
        init();
        addShutdownHook();
    }

    /**
     * 启动
     *
     * @throws Exception
     */
    private void init() throws Exception {

        logger.info("开始启动服务器...");

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {

                ChannelPipeline pipeline = ch.pipeline();
                // decoded
                pipeline.addLast(new LengthFieldBasedFrameDecoder(
                        MAX_FRAME_LENGTH, 0, 4, 0, 4));
                pipeline.addLast(new ProtobufDecoder(Protocol.Request
                        .getDefaultInstance()));
                pipeline.addLast(new StandardRequestDecoder());
                // encoded
                pipeline.addLast(new LengthFieldPrepender(4));
                pipeline.addLast(new ProtobufEncoder());

                pipeline.addLast(new MessageHandler());
            }

        });
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.bind(config.getIp(), config.getPort()).sync();
        logger.info("服务器已启动");
        logger.info("Binding port: " + config.getPort());
    }


    /**
     * 添加ShutdownHook
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("开始关闭netty服务器...");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }));

    }


    public static void main(String[] args) throws Exception {
    }
}

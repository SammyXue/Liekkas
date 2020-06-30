package com.liekkas.core.netty;

import com.liekkas.core.config.NettyServerConfig;
import com.liekkas.core.message.proto.Protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.apache.log4j.Logger;


public class NettyTcpServer {

    private static Logger logger = Logger.getLogger(NettyTcpServer.class);
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private NettyServerConfig config;

    public NettyTcpServer(NettyServerConfig config) {
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

        logger.info("netty server init start");

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {

                ChannelPipeline pipeline = ch.pipeline();
                // decoded
                pipeline.addLast(new LengthFieldBasedFrameDecoder(
                        config.getMaxFrameLength(), 0, 4, 0, 4));
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
        logger.info("netty server init finished");
        logger.info("Binding port: " + config.getPort());
    }


    /**
     * 添加ShutdownHook
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("netty server shuntdown start");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            logger.info("netty server shuntdown finish");

        }));

    }


    public static void main(String[] args) throws Exception {
    }
}

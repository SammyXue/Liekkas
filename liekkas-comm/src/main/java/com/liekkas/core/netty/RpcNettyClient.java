package com.liekkas.core.netty;

import com.liekkas.core.init.InitConstants;
import com.liekkas.core.message.Command;
import com.liekkas.core.message.MessageCreater;
import com.liekkas.core.message.StandardRequest;
import com.liekkas.core.message.proto.Protocol;
import com.liekkas.core.rpc.RpcCallback;
import com.liekkas.core.rpc.RpcFuture;
import com.liekkas.core.server.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.apache.log4j.Logger;

public class RpcNettyClient {
    private static final int MAX_FRAME_LENGTH = Integer.parseInt(InitConstants.severProperties.getProperty("server.maxFrameLength", "10240"));
    private static Logger logger = Logger.getLogger(InitConstants.class);

    String host;
    int port;
    Bootstrap bootstrap;
    EventLoopGroup group;
    Channel channel;


    public RpcNettyClient(Server server) {
        this(server.getIp(), server.getPort());
    }

    private RpcNettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            init();
        } catch (InterruptedException e) {
            logger.error("RpcNettyClient init fail", e);
        }
    }

    public void init() throws InterruptedException {
        bootstrap = new Bootstrap();

        group = new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class).option(
                ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                // decoded
                pipeline.addLast(new LengthFieldBasedFrameDecoder(
                        MAX_FRAME_LENGTH, 0, 4, 0, 4));
                pipeline.addLast(new ProtobufDecoder(Protocol.Response
                        .getDefaultInstance()));
                // encoded
                pipeline.addLast(new LengthFieldPrepender(4));
                pipeline.addLast(new ProtobufEncoder());
                pipeline.addLast(new ClientHandler());
                pipeline.addLast(new RpcClientHandler());

            }
        });
        channel = bootstrap.connect(host, port).sync().channel();

    }

    public RpcFuture send(StandardRequest request, RpcCallback... callbacks) {
        return send(request.getProtocolRequest(), request.getRequestId(), callbacks);
    }

    private RpcFuture send(Protocol.Request request, String requestId, RpcCallback... callbacks) {
        if (!isConnected()) {
            throw new IllegalStateException("rpc connection is disconnected ");
        }
        RpcFuture rpcFuture = new RpcFuture();

        for (RpcCallback callback : callbacks) {
            rpcFuture.addCallback(callback);
        }
        RpcClientHandler.map.put(requestId, rpcFuture);
        channel.writeAndFlush(request);


        return rpcFuture;
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    public void shutdown() {
        /**
         * 在调用shutdownGracefully后 channel继续writeAndFlush居然不会抛出任何异常（当然肯定是发送失败），所以封装了isConnected状态
         */
        group.shutdownGracefully();

    }

    public static void main(String[] args) throws Exception {
        RpcNettyClient rpcNettyClient = new RpcNettyClient("127.0.0.1", 5656);
        Protocol.Param userId = Protocol.Param.newBuilder().setKey("userId").setValue("123").build();
        Protocol.Param password = Protocol.Param.newBuilder().setKey("password").setValue("123").build();
        Protocol.Param sessionId = Protocol.Param.newBuilder().setKey("session").setValue("sessions").build();
        Protocol.Request request = MessageCreater.generateRequest(Command.Login, userId, password, sessionId);
    }
}

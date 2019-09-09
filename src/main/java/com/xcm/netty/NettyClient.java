package com.xcm.netty;

import com.xcm.message.Command;
import com.xcm.message.MessageCreater;
import com.xcm.proto.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClient implements Runnable {
	private static final int MAX_FRAME_LENGTH = 10240;


	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			// final ExtensionRegistry registry =
			// ExtensionRegistry.newInstance();
			// Respone.registerAllExtensions(registry);
			b.group(group);
			b.channel(NioSocketChannel.class).option(
					ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
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

				}
			});
			Channel channel = b.connect("127.0.0.1", 5656).sync().channel();

			Protocol.Param userId = Protocol.Param.newBuilder().setKey("userId").setValue("123").build();
			Protocol.Param password = Protocol.Param.newBuilder().setKey("password").setValue("123").build();
			Protocol.Param sessionId = Protocol.Param.newBuilder().setKey("session").setValue("sessions").build();

			channel.writeAndFlush(MessageCreater.generateRequest(Command.Login,userId,password,sessionId));


			channel.closeFuture().sync();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new Thread(new NettyClient(), ">>>this thread ").start();
	}
}

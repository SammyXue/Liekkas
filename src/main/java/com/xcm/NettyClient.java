package com.xcm;

import io.netty.bootstrap.Bootstrap;
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
					pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0,
							4, 0, 4));

					// encoded
					pipeline.addLast(new LengthFieldPrepender(4));
					pipeline.addLast(new IdleStateHandler(20, 10, 5));
					pipeline.addLast(new ClientHandler());

				}
			});
			ChannelFuture f = b.connect("127.0.0.1", 5656).sync();

			f.channel().closeFuture().sync();

		} catch (Exception e) {

		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new Thread(new NettyClient(), ">>>this thread ").start();
	}
}

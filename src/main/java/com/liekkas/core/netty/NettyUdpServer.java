package com.liekkas.core.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.nio.charset.StandardCharsets;

public class NettyUdpServer {
    private EventLoopGroup group;

    public NettyUdpServer() {
        try {
            init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() throws InterruptedException {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                //引导该 NioDatagramChannel（无连接的）
                .channel(NioDatagramChannel.class)
                // 设置 SO_BROADCAST 套接字选项
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new UdpServerHandler());
        bootstrap.bind(2555).sync().channel().closeFuture().await();

    }

    private static class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
            ByteBuf buf = packet.copy().content();
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, StandardCharsets.UTF_8);
            System.out.println(body);//打印收到的信息
            //向客户端发送消息
            String json = "来自服务端";
            // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            DatagramPacket data = new DatagramPacket(Unpooled.copiedBuffer(bytes), packet.sender());
            ctx.writeAndFlush(data);//向客户端发送消息
        }
    }

    public static void main(String[] args) {
        new NettyUdpServer();
    }

}

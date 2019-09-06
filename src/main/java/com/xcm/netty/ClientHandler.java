package com.xcm.netty;

import com.xcm.proto.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {






        System.out.println("开始同步~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端断开连接============" + ctx.channel());
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        System.out.println("received msg:");
        System.out.println(msg);
//        ctx.writeAndFlush(msg);

        /*
         * Person.User user = (User) msg; logger.info("client接收到服务器返回的消息:" +
         * user.getId()); logger.info("client接收到服务器返回的消息:" + user.getName());
         */
        // channelActive(ctx);
        /*
         * Respone.ResponeMsg responeMsg = (ResponeMsg) msg; Login.LoginRes
         * loginRes = Login.LoginRes .parseFrom(responeMsg.getBody());
         * System.out.println("结果" + loginRes.getResCode());
         */
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        /*
         * if (evt instanceof IdleStateEvent) { IdleStateEvent e =
         * (IdleStateEvent) evt; switch (e.state()) { case WRITER_IDLE:
         * System.out.println("WRITER_IDLE"); break; case ALL_IDLE:
         * System.out.println("ALL_IDLE"); break; case READER_IDLE:
         * System.out.println("READER_IDLE"); break; default: break; } }
         */
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

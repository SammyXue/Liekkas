package com.xcm.netty;

import com.xcm.proto.Protocol;
import com.xcm.rpc.RpcCallback;
import com.xcm.rpc.RpcFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    static Map<String, RpcFuture> map = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Protocol.Response response = (Protocol.Response) msg;
        RpcFuture rpcFuture = map.remove(response.getHeader().getResponseId());
        if (rpcFuture != null) {
            rpcFuture.done(response);

        }
    }

}

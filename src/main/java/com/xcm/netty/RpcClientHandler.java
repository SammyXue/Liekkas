package com.xcm.netty;

import com.xcm.proto.Protocol;
import com.xcm.rpc.RpcCallback;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    static Map<String, RpcCallback> map = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Protocol.Response response = (Protocol.Response) msg;
        RpcCallback rpcCallback = map.remove(response.getHeader().getResponseId());
        if (rpcCallback != null) {
            if (response.getHeader().getState() == 1) {
                rpcCallback.onSuccess(response);
            } else {
                rpcCallback.onFail(response);

            }
        }
    }

}

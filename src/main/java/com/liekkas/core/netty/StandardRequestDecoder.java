package com.liekkas.core.netty;

import com.liekkas.core.message.RpcRequest;
import com.liekkas.core.message.StandardRequest;
import com.liekkas.core.message.proto.Protocol;
import com.liekkas.core.constants.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class StandardRequestDecoder extends MessageToMessageDecoder<Protocol.Request> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Protocol.Request msg, List<Object> out) throws Exception {
        StandardRequest request = null;
        if (msg.getHeader().getType() == MessageType.REQUEST.getType()) {
            request = new StandardRequest(msg);
        } else if (msg.getHeader().getType() == MessageType.RPC.getType()) {

            request = new RpcRequest(msg);
        }
        if (request == null) {
            throw new RuntimeException("unknown Type in request");
        }
        out.add(request);
    }
}

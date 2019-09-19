package com.xcm.netty;

import com.xcm.message.RpcRequest;
import com.xcm.message.StandardRequest;
import com.xcm.proto.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class StandardRequestDecoder extends MessageToMessageDecoder<Protocol.Request> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Protocol.Request msg, List<Object> out) throws Exception {
        StandardRequest request = null;
        if (msg.getHeader().getType()==1){
            request = new StandardRequest(msg);
        }else if (msg.getHeader().getType()==2){

            request = new RpcRequest(msg);
        }
        if (request==null){
            throw new RuntimeException("unknown Type in request");
        }
        out.add(request);
    }
}

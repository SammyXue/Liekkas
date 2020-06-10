package com.liekkas.core.action;

import com.google.protobuf.ByteString;
import com.liekkas.core.message.MsgType;
import com.liekkas.core.message.RpcRequest;
import com.liekkas.core.message.StandardRequest;
import com.liekkas.core.message.State;
import com.liekkas.core.proto.Protocol;

public class BaseAction {
    public static final int VERSION = 1;

    public Protocol.Response getResult(byte[] body, StandardRequest request) {
        String requestId = request instanceof RpcRequest?((RpcRequest) request).getRequestId():"";
        Protocol.ResponseHeader header = Protocol.ResponseHeader.newBuilder().setVersion(VERSION)
                .setMsgType(MsgType.ECHO.getValue())
                .setState(State.SUCCESS.getValue())
                .setResponseId(requestId)
                .build();
        Protocol.Response response = Protocol.Response.newBuilder()
                .setHeader(header)
                .setBody(ByteString.copyFrom(body)).build();
        return response;
    }


    public Protocol.Response getResult(byte[] body) {
        Protocol.ResponseHeader header = Protocol.ResponseHeader.newBuilder().setVersion(VERSION)
                .setMsgType(MsgType.ECHO.getValue())
                .setState(State.SUCCESS.getValue())
                .build();
        Protocol.Response response = Protocol.Response.newBuilder()
                .setHeader(header)
                .setBody(ByteString.copyFrom(body)).build();
        return response;
    }
}

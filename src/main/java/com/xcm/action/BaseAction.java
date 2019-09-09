package com.xcm.action;

import com.google.protobuf.ByteString;
import com.xcm.message.MsgType;
import com.xcm.message.State;
import com.xcm.proto.Protocol;

public class BaseAction {
    public static final int VERSION = 1;

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

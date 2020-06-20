package com.liekkas.core.message;

import com.liekkas.core.message.proto.Protocol;

import java.util.UUID;

public class MessageCreater {

    public static final int VERSION = 1;

    /**
     * 客户端请求
     *
     * @param command
     * @param params
     * @return
     */
    public static Protocol.Request generateRequest(Command command, Protocol.Param... params) {
        Protocol.RequestHeader header = Protocol.RequestHeader.newBuilder()
                .setVersion(VERSION)
                .setCommand(command.name())
                .setType(1)
                .build();
        Protocol.RequestBody.Builder bodyBuilder = Protocol.RequestBody.newBuilder();
        for (Protocol.Param param : params) {
            bodyBuilder.addParam(param);

        }
        Protocol.RequestBody body = bodyBuilder.build();
        Protocol.Request request = Protocol.Request.newBuilder()
                .setHeader(header)
                .setBody(body)
                .build();
        return request;
    }

    /**
     * rpc请求
     *
     * @param command
     * @param args
     * @return
     */
    public static Protocol.Request generateRPCRequest(Command command, Object... args) {
        Protocol.RequestHeader header = Protocol.RequestHeader.newBuilder()
                .setVersion(VERSION)
                .setCommand(command.name())
                .setType(2)
                .setRequestId(UUID.randomUUID().toString())
                .build();
        Protocol.RequestBody.Builder bodyBuilder = Protocol.RequestBody.newBuilder();
        UUID uuid = UUID.randomUUID();

        int paramIndex = 0;
        for (Object arg : args) {
            Protocol.Param param = Protocol.Param.newBuilder()
                    .setKey("param" + paramIndex++)
                    .setValue(String.valueOf(arg))
                    .build();
            bodyBuilder.addParam(param);
        }


        Protocol.RequestBody body = bodyBuilder.build();
        Protocol.Request request = Protocol.Request.newBuilder()
                .setHeader(header)
                .setBody(body)
                .build();

        return request;
    }

}

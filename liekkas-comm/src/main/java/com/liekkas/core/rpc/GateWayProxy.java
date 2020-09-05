package com.liekkas.core.rpc;

import com.liekkas.core.message.Command;
import com.liekkas.core.message.MessageCreater;
import com.liekkas.core.message.StandardRequest;
import com.liekkas.core.netty.RpcNettyClient;
import com.liekkas.core.message.proto.Protocol;
import com.liekkas.core.server.Server;

import java.lang.invoke.SerializedLambda;

public class GateWayProxy {

    private RpcNettyClient rpcNettyClient;
    private Server target;

    public GateWayProxy(RpcNettyClient rpcNettyClient, Server target) {
        this.rpcNettyClient = rpcNettyClient;
        this.target = target;
    }

    public RpcFuture sendRpc(Command command, Object... args) {
        Protocol.Request request = MessageCreater.generateRPCRequest(target, command, args);
        return rpcNettyClient.send(new StandardRequest(request));
    }

    public RpcFuture sendRpc(Command command, RpcCallback callback, Object... args) {
        Protocol.Request request = MessageCreater.generateRPCRequest(target, command, args);
        return rpcNettyClient.send(new StandardRequest(request), callback);
    }

    public RpcFuture sendRequest(Command command, Protocol.Param... params) {
        Protocol.Request request = MessageCreater.generateRequest(target, command, params);
        return rpcNettyClient.send(new StandardRequest(request));
    }

    public RpcFuture sendRequest(Command command, RpcCallback callback, Protocol.Param... params) {
        Protocol.Request request = MessageCreater.generateRequest(target, command, params);
        return rpcNettyClient.send(new StandardRequest(request), callback);
    }

    public void disconnect() {
        rpcNettyClient.shutdown();
    }


//    if(isAsync)
//
//    { // Send asynchronously
//        producer.send(new ProducerRecord<>(topic,
//                messageStr), new DemoCallBack(startTime, "temp", messageStr));
//    } else
//
//    { // Send synchronously
//        try {
//            producer.send(new ProducerRecord<>(topic,
//                    messageStr)).get();
//            System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
}

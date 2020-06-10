package com.liekkas.core.rpc;

import com.liekkas.core.message.Command;
import com.liekkas.core.message.MessageCreater;
import com.liekkas.core.message.RpcRequest;
import com.liekkas.core.netty.RpcNettyClient;
import com.liekkas.core.message.proto.Protocol;

public class RpcProxy {

    private RpcNettyClient rpcNettyClient;

    public RpcProxy(RpcNettyClient rpcNettyClient) {
        this.rpcNettyClient = rpcNettyClient;
    }

    public RpcFuture send(Command command, Object... args) {
        Protocol.Request request = MessageCreater.generateRPCRequest(command, args);
        return rpcNettyClient.send(new RpcRequest(request));
    }

    public RpcFuture send(Command command, RpcCallback callback, Object... args) {
        Protocol.Request request = MessageCreater.generateRPCRequest(command, args);
        return rpcNettyClient.send(new RpcRequest(request), callback);
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

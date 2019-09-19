package com.xcm.rpc;

import com.xcm.message.Command;
import com.xcm.message.MessageCreater;
import com.xcm.message.RpcRequest;
import com.xcm.netty.RpcNettyClient;
import com.xcm.proto.Protocol;

import java.util.concurrent.Future;

public class RpcProxy {

    private RpcNettyClient rpcNettyClient;

    public RpcProxy(RpcNettyClient rpcNettyClient) {
        this.rpcNettyClient = rpcNettyClient;
    }

    private Future send(Command command, Object... args) {

        return null;
    }

    private void send(Command command, RpcCallback callback, Object... args) {
        Protocol.Request request = MessageCreater.generateRPCRequest(command, args);
        rpcNettyClient.send(new RpcRequest(request), callback);
    }

    public static void main(String[] args) {
         new RpcProxy(new RpcNettyClient("127.0.0.1", 5656)).send(Command.Login, new RpcCallback() {
             @Override
             public void onFail(Protocol.Response response) {
                 System.out.println("onFail");
             }

             @Override
             public void onSuccess(Protocol.Response response) {
                 System.out.println("onSuccess");
             }
         });

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
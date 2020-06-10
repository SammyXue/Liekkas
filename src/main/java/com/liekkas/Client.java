package com.liekkas;

import com.liekkas.core.message.Command;
import com.liekkas.core.netty.RpcNettyClient;
import com.liekkas.core.rpc.RpcFuture;
import com.liekkas.core.rpc.RpcProxy;

import java.util.concurrent.ExecutionException;

public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RpcProxy proxy = new RpcProxy(new RpcNettyClient("127.0.0.1", 5656));
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                RpcFuture rpcFuture = proxy.send(Command.Login);
            }

            ).start();
        }


    }
}

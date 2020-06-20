package com.liekkas;

import com.liekkas.core.init.InitConstants;
import com.liekkas.core.message.Command;
import com.liekkas.core.netty.RpcNettyClient;
import com.liekkas.core.rpc.RpcFuture;
import com.liekkas.core.rpc.RpcProxy;
import com.liekkas.core.server.Server;

import java.util.concurrent.ExecutionException;

public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Server server = new Server();
        server.setIp("0.0.0.0");
        server.setPort(5656);

        RpcProxy proxy = new RpcProxy(new RpcNettyClient(server));
        try {

        for (int i = 0; i < 100; i++) {
//            new Thread(() -> {
                RpcFuture rpcFuture = proxy.send(Command.Login,1,"");
                try {
                    rpcFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
//            }

//            ).start();
        }

        }finally {
            proxy.disconnect();
        }



    }
}

package com.liekkas;

import com.liekkas.core.constants.ServerType;
import com.liekkas.core.message.Command;
import com.liekkas.core.netty.RpcNettyClient;
import com.liekkas.core.rpc.GateWayProxy;
import com.liekkas.core.rpc.RpcFuture;
import com.liekkas.core.rpc.GateWayProxy;
import com.liekkas.core.server.Server;

import java.util.concurrent.ExecutionException;

public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Server server = new Server();
        server.setIp("0.0.0.0");
        server.setPort(5657);//kcptun 加速


        Server target = new Server();
        target.setServerId(1);
        target.setServerType(ServerType.LOGIN.getType());
        GateWayProxy proxy = new GateWayProxy(new RpcNettyClient(server), target);
        try {

            for (int i = 0; i < 100; i++) {
//            new Thread(() -> {
                RpcFuture rpcFuture = proxy.sendRpc(Command.Login, 1, "");
                try {
                    rpcFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

//            ).start();
//        }

        } finally {
            proxy.disconnect();
        }


    }
}

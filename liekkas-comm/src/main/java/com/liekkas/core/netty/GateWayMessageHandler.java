package com.liekkas.core.netty;

import com.liekkas.core.BeanGetter;
import com.liekkas.core.constants.ServerType;
import com.liekkas.core.init.ZkService;
import com.liekkas.core.message.MessageCreater;
import com.liekkas.core.message.StandardRequest;
import com.liekkas.core.message.proto.Protocol;
import com.liekkas.core.rpc.RpcFuture;
import com.liekkas.core.server.Server;
import com.liekkas.core.server.ServerManager;
import io.netty.channel.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GateWayMessageHandler extends ChannelInboundHandlerAdapter {
    static Map<String, RpcNettyClient> map = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        StandardRequest request = (StandardRequest) msg;

        String serverKey = ServerType.getNameByType(request.getProtocolRequest().getHeader().getServerType()) + "_" + request.getProtocolRequest().getHeader().getServerId();
        RpcNettyClient client = map.get(serverKey);
        if (client == null) {
            Server server = ServerManager.getInstance().getServer(serverKey);
            client = new RpcNettyClient(server);
            map.put(serverKey, client);
        }
        RpcFuture future = client.send(request);
        Protocol.Response response = future.get();
        ctx.writeAndFlush(response);

    }


}

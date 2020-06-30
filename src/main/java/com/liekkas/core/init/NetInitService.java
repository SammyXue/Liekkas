package com.liekkas.core.init;

import com.liekkas.core.config.NettyServerConfig;
import com.liekkas.core.config.NettyServerConfigImpl;
import com.liekkas.core.netty.NettyTcpServer;
import com.liekkas.core.server.Server;
import com.liekkas.core.server.ServerManager;
import org.springframework.stereotype.Component;


@Component
public class NetInitService implements InitService {

    @Override
    public void init() throws Exception {

        NettyServerConfig nettyServerConfig = new NettyServerConfigImpl(InitConstants.severProperties);
        NettyTcpServer nettyServer = new NettyTcpServer(nettyServerConfig);
        nettyServer.start();
        Server server = new Server(nettyServerConfig);
        ServerManager.getInstance().registerSelf(server);
    }

    /**
     * 在 zkService前初始化
     *
     * @return
     */
    @Override
    public int priority() {
        return Integer.MAX_VALUE - 1;
    }
}

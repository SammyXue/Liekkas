package com.liekkas.core.init;

import com.liekkas.core.config.NettyServerConfigImpl;
import com.liekkas.core.netty.NettyServer;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.Properties;

@Component
public class NetInitService implements InitService {

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();

        properties.load((new FileInputStream(InitConstants.RESOURCE_PATH + "server.properties")));

        NettyServer nettyServer = new NettyServer(new NettyServerConfigImpl(properties));
        nettyServer.start();
    }
}

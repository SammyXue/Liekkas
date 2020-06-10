package com.liekkas.core.config;

import com.liekkas.Main;
import com.liekkas.core.constants.ServerType;
import org.apache.log4j.Logger;

import java.util.Properties;

public class NettyServerConfigImpl implements NettyServerConfig {
    private static Logger logger = Logger.getLogger(Main.class);

    private int serverId;
    private int serverType;
    private String ip;
    private int port;

    public NettyServerConfigImpl(Properties properties) {
        try {
            this.serverId = Integer.parseInt(properties.getProperty("server.id"));
            this.serverType = Integer.parseInt(properties.getProperty("server.type"));
            this.ip = properties.getProperty("server.ip");
            this.port = Integer.parseInt(properties.getProperty("server.port"));
        } catch (Exception e) {
            logger.error("error format", e);
        }
    }

    @Override
    public int getBossGroupSize() {
        return 1;
    }

    @Override
    public int getWorkerGroupSize() {
        return 2 * Runtime.getRuntime().availableProcessors() * 2;
    }

    @Override
    public int getServerId() {
        return serverId;
    }

    @Override
    public int getServerType() {
        return serverType;
    }

    @Override
    public String getServerName() {
        return ServerType.getNameByType(serverType) + "_" + serverId;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }
}

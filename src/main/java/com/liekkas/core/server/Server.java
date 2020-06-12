package com.liekkas.core.server;

import com.alibaba.fastjson.JSON;
import com.liekkas.core.config.NettyServerConfig;
import com.liekkas.core.config.ServerConfig;
import com.liekkas.core.constants.ServerType;

import java.util.Objects;

public class Server {
    private int serverId;
    private int serverType;
    private String ip;
    private int port;

    public Server() {
    }

    public Server(ServerConfig serverConfig) {
        this.serverId = serverConfig.getServerId();
        this.serverType = serverConfig.getServerType();
        this.ip = serverConfig.getIp();
        this.port = serverConfig.getPort();
    }

    public static Server parse(byte[] bytes) {
        Server s = JSON.parseObject(new String(bytes), Server.class);
        return s;
    }

    public int getServerId() {
        return serverId;
    }

    public int getServerType() {
        return serverType;
    }

    public String getServerName() {
        return ServerType.getNameByType(serverType) + "_" + serverId;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte[] toJson() {
        String s = JSON.toJSONString(this);
        return s.getBytes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return serverId == server.serverId &&
                serverType == server.serverType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, serverType);
    }

    @Override
    public String toString() {
        return "Server{" +
                "serverName =" + getServerName() +
                '}';
    }
}

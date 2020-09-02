package com.liekkas.core.server;

import com.liekkas.core.init.InitConstants;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerManager {
    private static Logger logger = Logger.getLogger(ServerManager.class);

    private static ServerManager INSTANCE = new ServerManager();

    public static ServerManager getInstance() {
        return INSTANCE;
    }


    private Map<String,Server> serverMap = new ConcurrentHashMap<>();

    private Server self;

    public Server getSelf() {
        return self;
    }


    public void registerSelf(Server server) {
        self = server;
    }


    public Server getServer(String key){
       return serverMap.get(key);
    }
    public void remove(Server server) {
        serverMap.remove(server.getServerName());
    }

    public void add(Server server) {
        serverMap.put(server.getServerName(),server);
    }

    public void removeAll() {
        serverMap.clear();
    }

    public void printAllServers() {
        logger.info("servers change");
        logger.info("----------------------------");

        for (Server server : serverMap.values()) {
            logger.info(server.toString());
        }
        logger.info("----------------------------");

    }
}

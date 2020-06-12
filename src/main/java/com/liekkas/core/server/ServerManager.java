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


    private List<Server> servers = new CopyOnWriteArrayList<>();

    private Server self;

    public Server getSelf() {
        return self;
    }


    public void registerSelf(Server server) {
        self = server;
    }


    public void remove(Server server) {
        servers.remove(server);
    }

    public void add(Server server) {
        servers.add(server);
    }

    public void removeAll() {
        servers.clear();
    }

    public void printAllServers() {
        logger.info("servers change");
        logger.info("----------------------------");

        for (Server server : servers) {
            logger.info(server.toString());
        }
        logger.info("----------------------------");

    }
}

package com.liekkas.core.init;

import com.liekkas.core.server.Server;
import com.liekkas.core.server.ServerManager;
import com.liekkas.zk.ZkTest;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class ZkService implements InitService {
    private static Logger logger = Logger.getLogger(ZkService.class);

    private CountDownLatch countDownLatch;
    private ZooKeeper zk;
    private String path;
    private String connectStr;

    private int timeOut;

    @Override
    public void init() throws Exception {
        /**
         * 配置
         * zk.address=47.103.205.195:2181
         * zk.path=/liekkas/servers
         * zk.sessionTimeout=5000
         */
        connectStr = InitConstants.severProperties.getProperty("zk.address");
        timeOut = Integer.parseInt(InitConstants.severProperties.getProperty("zk.sessionTimeout", "5000"));
        path = InitConstants.severProperties.getProperty("zk.path");
        connect();


        addShutdownHook();
    }

    /**
     * 添加ShutdownHook
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("zk close");
            try {
                zk.close();
            } catch (InterruptedException e) {
                logger.error("zk close error");
            }
        }));

    }

    private void connect() throws KeeperException, InterruptedException, IOException {
        countDownLatch = new CountDownLatch(1);
        zk = new ZooKeeper(connectStr, timeOut, new MyWatcher());
        countDownLatch.await();
        Server self = ServerManager.getInstance().getSelf();
        String serverPath = path + "/" + self.getServerName();
        if (zk.exists(serverPath, false) != null) {
            zk.close();
            throw new RuntimeException("zk node exists:" + serverPath+",please check your config");
        }
        //TODO:ACL待选择
        zk.create(serverPath, self.toJson(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

    }



    class MyWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            logger.debug("zk Watcher process start:" + event);

            try {
                if (Event.KeeperState.Disconnected == event.getState()) {
                    connect();
                }
                if (Event.KeeperState.SyncConnected == event.getState()) {
                    countDownLatch.countDown();
                    fetchAll();
                } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    fetchAll();
                } else if (event.getType() == Event.EventType.NodeDeleted) {
                    fetchAll();
                }


            } catch (Exception e) {
                logger.error("", e);
            }

            logger.debug("zk Watcher process finish:" + event);

        }
    }

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }

    private void fetchAll() throws KeeperException, InterruptedException {
        ServerManager.getInstance().removeAll();
        //会监听子节点增删
        List<String> children = zk.getChildren(path, true, null);
        for (String child : children) {
            //会监听节点数据变化
            byte[] data = zk.getData(path+"/" + child, true, null);

            ServerManager.getInstance().add(Server.parse(data));
        }
        ServerManager.getInstance().printAllServers();

    }

}

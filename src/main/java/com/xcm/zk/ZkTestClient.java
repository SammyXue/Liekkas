package com.xcm.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZkTestClient {
    static String ADDRESS = "47.103.205.195:2181";
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    static ZooKeeper zooKeeper;

    static class MyWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            System.out.println("process:" + event);
            System.out.println("----------------------------");

            try {
                //会监听子节点增删
                List<String> children = zooKeeper.getChildren("/xuecm", true, null);
                for (String child : children) {
                    //会监听节点数据变化
                    zooKeeper.getData("/xuecm/" + child, true, null);
                    System.out.println(child);
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("----------------------------");

        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new MyWatcher());
        System.out.println("state:" + zooKeeper.getState());


//        String path2 = zooKeeper.create(PREFIX, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//        System.out.println("success create znode:"+ path2);
        synchronized (ZkTestClient.class) {
            ZkTestClient.class.wait();
        }
        zooKeeper.close();
    }
}

package com.liekkas.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkTest {
    static String ADDRESS = "47.103.205.195:2181";
    static CountDownLatch countDownLatch = new CountDownLatch(1);

    static class MyWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                countDownLatch.countDown();
            }
            System.out.println("process:" + event);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, 5000, new MyWatcher());
        System.out.println("state:" + zooKeeper.getState());
        countDownLatch.await();

         zooKeeper.setData("/xuecm/test3", "1".getBytes(),-1);

        System.out.println("state:" + zooKeeper.getState());

//        String path2 = zooKeeper.create(PREFIX, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//        System.out.println("success create znode:"+ path2);
        synchronized (ZkTest.class){
            ZkTest.class.wait();
        }
        zooKeeper.close();
    }
}

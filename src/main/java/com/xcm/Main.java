package com.xcm;

import com.xcm.netty.MyNettyServer;

public class Main {
    public static void main(String[] args) {
        try {
            MyNettyServer.service();
            MyNettyServer.addShutdownHook();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

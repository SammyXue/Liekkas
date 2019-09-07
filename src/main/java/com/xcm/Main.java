package com.xcm;

import com.xcm.netty.MyNettyServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        try {
            MyNettyServer.service();
            MyNettyServer.addShutdownHook();
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

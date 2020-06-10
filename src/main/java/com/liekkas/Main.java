package com.liekkas;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {

            ClassPathXmlApplicationContext context =
                    new ClassPathXmlApplicationContext("applicationContext.xml");


        } catch (Exception e) {
            logger.error("error ",e);
        }
    }
}

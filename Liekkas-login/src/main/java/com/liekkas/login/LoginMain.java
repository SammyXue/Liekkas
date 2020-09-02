package com.liekkas.login;

import com.liekkas.core.exception.InitException;
import com.liekkas.core.init.InitServiceManager;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class LoginMain {
    private static Logger logger = Logger.getLogger(LoginMain.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context =
                    new ClassPathXmlApplicationContext("applicationContext.xml");

            InitServiceManager.getInstance().init();
        } catch (InitException e) {
            logger.error("init error", e);

        } catch (Exception e) {
            logger.error(" error", e);

        }
    }
}

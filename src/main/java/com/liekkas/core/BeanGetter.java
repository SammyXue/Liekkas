package com.liekkas.core;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.CountDownLatch;

public class BeanGetter  {
    static Logger logger = Logger.getLogger(ActionInvoker.class);

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        countDownLatch.countDown();
        logger.info("BeanGetter setApplicationContext success");
    }

    public static Object getBean(String beanName) {
        if (context != null) {
            return context.getBean(beanName);
        } else {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.error("getBean error ", e);
            }
            return getBean(beanName);
        }
    }
}

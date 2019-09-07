package com.xcm.service.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class CoreService implements ApplicationContextAware , InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext= applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBean(ActionInitManager.class).init();
        System.out.println();
    }
}

package com.liekkas.core;

import com.liekkas.core.init.NetInitManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CoreService implements ApplicationContextAware, InitializingBean {
    private static Logger logger = Logger.getLogger(NetInitManager.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        BeanGetter.setApplicationContext(applicationContext);
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        List<InitManager> list = new ArrayList<>();
        for (String beanName : allBeanNames) {
            Object bean = applicationContext.getBean(beanName);

            if (bean instanceof InitManager) {
                list.add(((InitManager) bean));

            }
        }
        list.sort((e1, e2) -> e2.priority() - e1.priority());
        for (InitManager initManager : list) {
            String name = initManager.getClass().getName();
            try {
                logger.info(name + " init start");
                initManager.init();
                logger.info(name + " init finish");

            } catch (Exception e) {
                logger.error(name + " init error ", e);
            }
        }

    }
}

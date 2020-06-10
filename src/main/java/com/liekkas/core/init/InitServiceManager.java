package com.liekkas.core.init;

import com.liekkas.core.BeanGetter;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitServiceManager implements ApplicationContextAware, InitializingBean {
    private static Logger logger = Logger.getLogger(NetInitService.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        BeanGetter.setApplicationContext(applicationContext);
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        List<InitService> list = new ArrayList<>();
        for (String beanName : allBeanNames) {
            Object bean = applicationContext.getBean(beanName);

            if (bean instanceof InitService) {
                list.add(((InitService) bean));

            }
        }
        list.sort((e1, e2) -> e2.priority() - e1.priority());
        for (InitService initService : list) {
            String name = initService.getClass().getName();
            try {
                logger.info(name + " init start");
                initService.init();
                logger.info(name + " init finish");

            } catch (Exception e) {
                logger.error(name + " init error ", e);
            }
        }

    }
}

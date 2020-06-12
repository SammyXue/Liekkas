package com.liekkas.core.init;

import com.liekkas.core.BeanGetter;
import com.liekkas.core.exception.InitException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.liekkas.core.init.InitConstants.INIT_SERVICE_MANAGER;

@Component(INIT_SERVICE_MANAGER)
public class InitServiceManager implements ApplicationContextAware, InitializingBean {
    private static Logger logger = Logger.getLogger(NetInitService.class);


    public static InitServiceManager getInstance() {
        return (InitServiceManager) BeanGetter.getBean(INIT_SERVICE_MANAGER);
    }



    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BeanGetter.setApplicationContext(applicationContext);
        try {
            InitConstants.severProperties.load((new FileInputStream(InitConstants.RESOURCE_PATH + "server.properties")));
        } catch (IOException e) {
            logger.error("load properties error", e);
        }


    }

    public void init() throws InitException {

        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        List<InitService> list = new ArrayList<>();
        for (String beanName : allBeanNames) {
            Object bean = applicationContext.getBean(beanName);

            if (bean instanceof InitService) {
                list.add(((InitService) bean));

            }
        }
        list.sort(Comparator.comparingInt(e -> e.priority()));
        for (InitService initService : list) {
            String clzName = initService.getClass().getName();
            try {
                logger.info(clzName + " init start");
                initService.init();
                logger.info(clzName + " init finish");

            } catch (Exception e) {
                logger.error(clzName + " init error ", e);
                throw new InitException(e, clzName);
            }
        }

    }
}

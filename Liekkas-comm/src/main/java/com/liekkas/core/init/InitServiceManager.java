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
    private static final String BANNER = "\n" +
            " ___       ___  _______   ___  __    ___  __    ________  ________      \n" +
            "|\\  \\     |\\  \\|\\  ___ \\ |\\  \\|\\  \\ |\\  \\|\\  \\ |\\   __  \\|\\   ____\\     \n" +
            "\\ \\  \\    \\ \\  \\ \\   __/|\\ \\  \\/  /|\\ \\  \\/  /|\\ \\  \\|\\  \\ \\  \\___|_    \n" +
            " \\ \\  \\    \\ \\  \\ \\  \\_|/_\\ \\   ___  \\ \\   ___  \\ \\   __  \\ \\_____  \\   \n" +
            "  \\ \\  \\____\\ \\  \\ \\  \\_|\\ \\ \\  \\\\ \\  \\ \\  \\\\ \\  \\ \\  \\ \\  \\|____|\\  \\  \n" +
            "   \\ \\_______\\ \\__\\ \\_______\\ \\__\\\\ \\__\\ \\__\\\\ \\__\\ \\__\\ \\__\\____\\_\\  \\ \n" +
            "    \\|_______|\\|__|\\|_______|\\|__| \\|__|\\|__| \\|__|\\|__|\\|__|\\_________\\\n" +
            "                                                            \\|_________|";
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
        logger.info(BANNER);
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
        list.sort(Comparator.comparingInt(InitService::priority));
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

package com.xcm.service.core;

import com.xcm.action.BaseAction;
import com.xcm.message.Path;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class ActionInitManager extends ApplicationObjectSupport implements InitManager {
    @Override
    public void init() {
        try {
            String[] allBeanNames = getApplicationContext().getBeanDefinitionNames();
            for (String beanName : allBeanNames) {
                Object bean = getApplicationContext().getBean(beanName);
                if (bean instanceof BaseAction) {
                    Class clazz = bean.getClass();
                    for (Method method : clazz.getDeclaredMethods()) {
                        Path path = method.getAnnotation(Path.class);
                        if (path != null) {
                            ActionInvoker actionInvoker = new ActionInvoker(method, bean);
                            MessageHandler.commandHandleMap.put(path.value(), actionInvoker);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

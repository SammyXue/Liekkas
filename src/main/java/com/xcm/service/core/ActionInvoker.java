package com.xcm.service.core;

import javax.xml.ws.spi.Invoker;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionInvoker {
    private Method method;
    private Object object;

    public ActionInvoker(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    public Object invoke(Object... params) throws InvocationTargetException, IllegalAccessException {
       return method.invoke(object,params);
    }
}

package com.xcm.service.core;

import com.xcm.message.Param;
import com.xcm.message.ParamTransfer;
import com.xcm.message.ParamTransferFactory;
import com.xcm.proto.Protocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActionInvoker {
    private Method method;
    private Object object;

    public ActionInvoker(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    public Object invoke(Protocol.Request request) throws InvocationTargetException, IllegalAccessException {

        return method.invoke(object, getParams(request));
    }

    public Object[] getParams(Protocol.Request request){
        Map<String,String> paramKeyValue= new HashMap<>();
        for (Protocol.Param param : request.getBody().getParamList()) {
            paramKeyValue.put(param.getKey(),param.getValue());
        }

        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];

            Param param = parameter.getAnnotation(Param.class);
            if (param==null){
                continue;
            }
            String key = param.value();


            ParamTransfer paramTransfer = ParamTransferFactory.getByParamType(parameter.getType());
            Object value = paramTransfer.transferTo(paramKeyValue.get(key));
            params[i] = value;
        }
        return params;
    }

}

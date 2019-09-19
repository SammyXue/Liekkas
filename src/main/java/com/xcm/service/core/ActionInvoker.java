package com.xcm.service.core;

import com.google.protobuf.ByteString;
import com.xcm.exception.StandardSystemException;
import com.xcm.message.*;
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

    public Object invoke(Protocol.Request request) {
        try {
            return method.invoke(object, getParams(request));

        } catch (Exception e) {
//            Log.err
            e.printStackTrace();
            return genErrorResponse(e);
        }
    }

    public Protocol.Response genErrorResponse(Exception e) {
        Protocol.ResponseHeader header = Protocol.ResponseHeader.newBuilder().setVersion(1)
                .setMsgType(MsgType.ECHO.getValue())
                .setState(State.FAIL.getValue())
                .build();
        String errMsg = "Error Occurred in Server";
        if (e instanceof InvocationTargetException) {
            if (((InvocationTargetException) e).getTargetException() instanceof StandardSystemException) {
                errMsg = ((StandardSystemException) ((InvocationTargetException) e).getTargetException()).getErrorMsg();
            }
        }


        Protocol.Response response = Protocol.Response.newBuilder()
                .setHeader(header)
                .setBody(ByteString.copyFrom(errMsg.getBytes())).build();
        return response;
    }

    public Object[] getParams(Protocol.Request request) {
        Map<String, String> paramKeyValue = new HashMap<>();
        for (Protocol.Param param : request.getBody().getParamList()) {
            paramKeyValue.put(param.getKey(), param.getValue());
        }

        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];

            Param param = parameter.getAnnotation(Param.class);
            if (param == null) {
                if (parameter.getType() == StandardRequest.class) {
                    params[i] = new StandardRequest(request);
                } else {
                    throw new RuntimeException("lack of  @param on parameter ,method :"+method.getDeclaringClass().getName()+"."+method.getName());
                }
            }


            String key = param.value();


            ParamTransfer paramTransfer = ParamTransferFactory.getByParamType(parameter.getType());
            Object value = paramTransfer.transferTo(paramKeyValue.get(key));
            params[i] = value;
        }
        return params;
    }

}

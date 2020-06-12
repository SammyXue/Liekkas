package com.liekkas.core.invoker;

import com.google.protobuf.ByteString;
import com.liekkas.core.exception.ServiceException;
import com.liekkas.core.message.*;
import com.liekkas.core.message.param.Param;
import com.liekkas.core.message.param.ParamTransfer;
import com.liekkas.core.message.param.ParamTransferFactory;
import com.liekkas.core.message.proto.Protocol;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ActionInvoker {
    static Logger logger = Logger.getLogger(ActionInvoker.class);
    Method method;
    Object object;

    public ActionInvoker(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    public Object invoke(StandardRequest request) {
        try {
            return method.invoke(object, getParams(request));

        } catch (Exception e) {
            logger.error("Error occurred in ActionInvoker ", e);
            return genErrorResponse(e, "");
        }
    }

    Protocol.Response genErrorResponse(Exception e, String requestId) {
        Protocol.ResponseHeader header = Protocol.ResponseHeader.newBuilder().setVersion(1)
                .setMsgType(MsgType.ECHO.getValue())
                .setState(State.FAIL.getValue())
                .setResponseId(requestId)
                .build();
        String errMsg = "Error Occurred in Server";
        if (e instanceof InvocationTargetException) {
            if (((InvocationTargetException) e).getTargetException() instanceof ServiceException) {
                errMsg = ((ServiceException) ((InvocationTargetException) e).getTargetException()).getErrorMsg();
            }
        }


        Protocol.Response response = Protocol.Response.newBuilder()
                .setHeader(header)
                .setBody(ByteString.copyFrom(errMsg.getBytes())).build();
        return response;
    }

    public Object[] getParams(StandardRequest request) {

        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];

            Param param = parameter.getAnnotation(Param.class);
            if (param == null) {
                if (parameter.getType() == StandardRequest.class) {
                    params[i] = request;
                    continue;
                } else {
                    throw new RuntimeException("lack of  @param on parameter ,method :" + method.getDeclaringClass().getName() + "." + method.getName());
                }
            }


            String key = param.value();


            ParamTransfer paramTransfer = ParamTransferFactory.getByParamType(parameter.getType());
            Object value = paramTransfer.transferTo(request.getParamValue(key));
            params[i] = value;
        }
        return params;
    }

}

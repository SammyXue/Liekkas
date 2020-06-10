package com.liekkas.core.invoker;

import com.liekkas.core.message.*;
import com.liekkas.core.message.proto.Protocol;

import java.lang.reflect.Method;

public class RpcInvoker extends ActionInvoker {
    public RpcInvoker(Method method, Object object) {
        super(method, object);
    }

    @Override
    public Object invoke(StandardRequest request) {
        try {
            RpcRequest rpcRequest = (RpcRequest) request;

            Protocol.Response temp = (Protocol.Response) method.invoke(object, getParams(request));

            Protocol.ResponseHeader header = Protocol.ResponseHeader.newBuilder().setVersion(temp.getHeader().getVersion())
                    .setMsgType(temp.getHeader().getMsgType())
                    .setState(temp.getHeader().getState())
                    .setResponseId(rpcRequest.getRequestId())
                    .build();
            Protocol.Response response = Protocol.Response.newBuilder()
                    .setHeader(header)
                    .setBody(temp.getBody()).build();
            return response;

        } catch (Exception e) {
//            Log.err
            logger.error("Error occurred in RpcInvoker ",e);
            return genErrorResponse(e,((RpcRequest) request).getRequestId());
        }
    }


//    @Override
//    public Object[] getParams(StandardRequest request) {
//        RpcRequest rpcRequest = (RpcRequest) request;
//        Object[] params = new Object[method.getParameterCount()];
//        //TODO：
//        for (int i = 0; i < method.getParameters().length; i++) {
//            Parameter parameter = method.getParameters()[i];
//
//            Param param = parameter.getAnnotation(Param.class);
//            if (parameter.getType() == StandardRequest.class) {
//                params[i] = rpcRequest;
//                continue;
//            }
//
//        }
//        return params;
//    }
}

package com.liekkas.core.invoker;

import com.liekkas.core.message.*;
import com.liekkas.core.message.param.ParamTransfer;
import com.liekkas.core.message.param.ParamTransferFactory;
import com.liekkas.core.message.proto.Protocol;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

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
            logger.error("Error occurred in RpcInvoker ", e);
            return genErrorResponse(e, ((RpcRequest) request).getRequestId());
        }
    }


    @Override
    public Object[] getParams(StandardRequest request) {
        Object[] params = new Object[method.getParameterCount()];
        List<Protocol.Param> paramList = request.getProtocolRequest().getBody().getParamList();
        int paramCount = (int) Arrays.stream(method.getParameters()).filter(e -> e.getType() != StandardRequest.class)
                .count();
        if (paramCount != paramList.size()) {
            throw new IllegalArgumentException("param count is not correct in request");
        }
        int index = 0;
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            if (parameter.getType() == StandardRequest.class) {
                params[i] = request;
                continue;
            } else {
                ParamTransfer paramTransfer = ParamTransferFactory.getByParamType(parameter.getType());
                Object value = paramTransfer.transferTo(paramList.get(index++).getValue());
                params[i] = value;
            }


        }
        return params;
    }
}

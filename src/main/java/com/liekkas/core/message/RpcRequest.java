package com.liekkas.core.message;

import com.liekkas.core.message.proto.Protocol;

import java.util.List;

public class RpcRequest extends StandardRequest {
    private String requestId;



    public RpcRequest(Protocol.Request protocolRequest) {
        super(protocolRequest);
        List<Protocol.Param> params= protocolRequest.getBody().getParamList();
        if (params==null){
            throw new UnsupportedOperationException("lack of requestId in rpcRequest");
        }
        String requestId = null ;
        for (Protocol.Param param : params) {
            if ("requestId".equals(param.getKey())){
                requestId = param.getValue();
            }
        }
        if (requestId==null){
            throw new UnsupportedOperationException("lack of requestId in rpcRequest");
        }
        this.requestId = requestId;

    }

    public String getRequestId() {
        return requestId;
    }

}

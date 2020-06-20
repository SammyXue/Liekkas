package com.liekkas.core.message;

import com.liekkas.core.message.proto.Protocol;

import java.util.List;

public class RpcRequest extends StandardRequest {
    private String requestId;



    public RpcRequest(Protocol.Request protocolRequest) {
        super(protocolRequest);
        if (!protocolRequest.getHeader().hasRequestId()){
            throw new RuntimeException("lack of requestId in rpcRequest");
        }
        this.requestId = protocolRequest.getHeader().getRequestId();

    }

    public String getRequestId() {
        return requestId;
    }

}

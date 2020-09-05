package com.liekkas.core.message;

import com.liekkas.core.init.SessionService;
import com.liekkas.core.message.proto.Protocol;
import com.liekkas.core.session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class StandardRequest {


    private String requestId;

    private Session session;
    private Response response;
    private Protocol.Request protocolRequest;
    private Map<String, String> params = new HashMap<>();

    private static final String SESSION_KEY = "session";

    public StandardRequest(Protocol.Request protocolRequest) {
        this.protocolRequest = protocolRequest;
        for (Protocol.Param param : protocolRequest.getBody().getParamList()) {
            params.put(param.getKey(), param.getValue());
        }
        if (params.containsKey(SESSION_KEY)) {
            session = SessionService.getInstance().getBySessionId(params.get(SESSION_KEY),false);
        }
        this.requestId = protocolRequest.getHeader().getRequestId()+ "";

    }

    public Protocol.Request getProtocolRequest() {
        return protocolRequest;
    }

    public Session getSession() {
        return session;
    }

    public String getParamValue(String key) {
        return params.get(key);
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return "StandardRequest{" +
                "protocolRequest=" + protocolRequest +
                '}';
    }
}

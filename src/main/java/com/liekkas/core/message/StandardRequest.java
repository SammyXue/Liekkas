package com.liekkas.core.message;

import com.liekkas.core.proto.Protocol;
import com.liekkas.core.session.Session;
import com.liekkas.core.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class StandardRequest {
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
            session = SessionManager.getInstance().getBySessionId(params.get(SESSION_KEY),false);
        }
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
}

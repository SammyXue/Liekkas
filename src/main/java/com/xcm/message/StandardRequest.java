package com.xcm.message;

import com.xcm.proto.Protocol;
import com.xcm.service.core.SessionManager;

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

    public Session getSession() {
        return session;
    }

}

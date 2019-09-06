package com.xcm.message;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Session {
    private String sessionId;
    private Channel channel;
    private long expireTime;
    /**
     * The collection of user data attributes associated with this Session.
     */
    protected ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<>();

    public Session(Channel channel) {
        this.channel = channel;

    }
}

package com.liekkas.core.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private SessionService manager;
    private String sessionId;
    private long nextExpireTime;
    private long expireTime;

    private static final long DEFAULT_EXPIRETIME = 30*60*1000;

    /**
     * The collection of user data attributes associated with this Session.
     */
    protected Map<String, Object> attributes = new ConcurrentHashMap<>();

    public Session(String sessionId, SessionService manager) {
        this.manager =manager;
        this.sessionId = sessionId;
        this.expireTime = DEFAULT_EXPIRETIME;
        this.nextExpireTime = System.currentTimeMillis()+expireTime;
    }

    public String getSessionId() {
        return sessionId;
    }


    /**
     * 设置属性
     *
     * @param key
     * @param value
     */
    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * 移除属性
     *
     * @param key
     */
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    /**
     * 重新设置过期时间
     */
    public void makeActive() {
        nextExpireTime = System.currentTimeMillis() + expireTime;
    }

    /**
     * 是否过期
     *
     * @return
     */
    public boolean isExpired() {
        return isExpired(true);
    }

    /**
     * 是否过期
     *
     * @param removeFromManager 是否从manager移除
     * @return
     */
    public boolean isExpired(boolean removeFromManager) {
        if (nextExpireTime < System.currentTimeMillis()) {
            if (removeFromManager) {
                manager.remove(this);
            }
            return true;
        }
        return false;
    }

}

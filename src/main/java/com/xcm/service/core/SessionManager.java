package com.xcm.service.core;

import com.xcm.message.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static SessionManager INSTANCE = new SessionManager();

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    private SessionManager() {
        Thread sessionClearThread = new Thread(new SessionRunnable(), "sessionClearThread");
        sessionClearThread.start();
    }


    public Session getBySessionId(String sessionId, boolean createNew) {

        Session session = sessions.get(sessionId);
        if (session != null && session.isExpired()) {
            if (createNew) {
                return createNewSession(sessionId);
            }
            return null;
        }
        return session;

    }

    public void remove(Session session) {
        if (session != null) {
            sessions.remove(session.getSessionId());
        }

    }

    public String generateSessionId() {
        return "sessions";
    }

    public Session createNewSession() {
        return createNewSession(generateSessionId());
    }

    private Session createNewSession(String sessionId) {
        Session session = new Session(sessionId, this);
        sessions.put(session.getSessionId(), session);
        return session;
    }

    class SessionRunnable implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    for (Map.Entry<String, Session> entry : sessions.entrySet()) {
                        entry.getValue().isExpired(true);
                    }

                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();//注意：因为在捕捉到InterruptedException异常的时候就会自动的中断标志置为了false，
                    e.printStackTrace();
                }
            }
        }
    }

}

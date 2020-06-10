package com.liekkas.core.session;

import com.liekkas.core.BeanGetter;
import com.liekkas.core.init.InitService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.liekkas.core.init.InitConstants.SESSION_MANAGER_NAME;

@Component(SESSION_MANAGER_NAME)
public class SessionService implements InitService {

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static SessionService getInstance() {
        return (SessionService) BeanGetter.getBean(SESSION_MANAGER_NAME);
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


    @Override
    public void init() throws Exception {
        Thread sessionClearThread = new Thread(new SessionRunnable(), "sessionClearThread");
        sessionClearThread.start();
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

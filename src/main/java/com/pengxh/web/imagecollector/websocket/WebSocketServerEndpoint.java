package com.pengxh.web.imagecollector.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket客户端建立连接的地址
 *
 * @author a203
 */
@Slf4j
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServerEndpoint {

    private Session session;

    private static final CopyOnWriteArraySet<WebSocketServerEndpoint> WEB_SOCKETS = new CopyOnWriteArraySet<>();
    private static final Map<String, Session> SESSION_POOL = new HashMap<>();

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        this.session = session;
        WEB_SOCKETS.add(this);
        SESSION_POOL.put(userId, session);
        log.info(userId + "【websocket消息】有新的连接，总数为:" + WEB_SOCKETS.size());
    }

    @OnClose
    public void onClose() {
        WEB_SOCKETS.remove(this);
        log.info("【websocket消息】连接断开，总数为:" + WEB_SOCKETS.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端消息:" + message);
    }

    /**
     * 广播群发消息
     */
    public void sendAllMessage(String message) {
        for (WebSocketServerEndpoint socketHandler : WEB_SOCKETS) {
            log.info("【websocket消息】广播消息:" + message);
            try {
                socketHandler.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendListMessage(List<String> userIds, String message) {
        log.info("【websocket消息】列表消息:" + message);
        for (String userId : userIds) {
            Session session = SESSION_POOL.get(userId);
            if (session != null) {
                try {
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 给特定用户发送消息
     */
    public void sendOneMessage(String userId, String message) {
        log.info("【websocket消息】单点消息:" + message);
        Session session = SESSION_POOL.get(userId);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketServerEndpoint that = (WebSocketServerEndpoint) o;
        return Objects.equals(session, that.session);
    }
}

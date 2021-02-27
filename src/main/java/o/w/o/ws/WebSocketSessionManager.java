package o.w.o.ws;

import lombok.extern.log4j.Log4j2;
import o.w.o.ws.endpoint.EntryEndpoint;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSessionManager
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/1/21 17:39
 */
@Log4j2
@Component
public class WebSocketSessionManager {
    private static WebSocketSessionManager wsManger;
    /**
     * concurrent 包的线程安全 Set，用来存放每个客户端对应的 MyWebSocket 对象。
     */
    private final ConcurrentHashMap<String, EntryEndpoint> webSocketSessionManager = new ConcurrentHashMap<>();

    public synchronized static WebSocketSessionManager getWebSocketSessionManager() {
        if (WebSocketSessionManager.wsManger == null) {
            WebSocketSessionManager.wsManger = new WebSocketSessionManager();
        }

        return WebSocketSessionManager.wsManger;
    }

    public synchronized void register(String sid, EntryEndpoint session) {
        this.webSocketSessionManager.put(sid, session);
    }

    public Optional<EntryEndpoint> access(String sid) {
        return Optional.of(this.webSocketSessionManager.get(sid));
    }

    public synchronized void revoke(String sid) {
        this.webSocketSessionManager.remove(sid);
    }

    public synchronized void p2p(String message, String sid) {
        this.access(sid).ifPresent(session -> {
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void broadcast(String message) {
        for (var item : webSocketSessionManager.entrySet()) {
            EntryEndpoint socketSession = item.getValue();
            String socketSessionKey = item.getKey();
            try {
                socketSession.sendMessage(message);
                logger.info("websocket[broadcast] sid -> {}", socketSessionKey);
            } catch (Exception e) {
                logger.warn("websocket[broadcast] e -> {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前在线连接数。应该把它设计成线程安全的。
     */
    public synchronized int connectedCounts() {
        return this.webSocketSessionManager.size();
    }
}

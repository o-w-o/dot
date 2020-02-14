package ink.o.w.o.api.websocket;

import ink.o.w.o.util.JSONHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 12
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/1/21 17:39
 */
@Log4j2
@Component
@PreAuthorize("hasRole('ROLE_USER')")
@ServerEndpoint("/websocket/{sid}")
public class WebSocketSession {
  @Resource
  private JSONHelper jsonHelper;

  private WebSocketSessionManager webSocketSessionManager = WebSocketSessionManager.getWebSocketSessionManager();


  /**
   * 与某个客户端的连接会话，需要通过它来给客户端发送数据。
   */
  private Session session;
  private String sid = "";

  @OnOpen
  public void onOpen(Session session, @PathParam("sid") String sid) {
    this.session = session;
    this.sid = sid;

    webSocketSessionManager.register(sid, this);
    logger.info("有新窗口开始监听: [ {} ] ,当前在线人数为 [ {} ]", sid, webSocketSessionManager.connectedCounts());

    try {
      this.sendMessage("连接成功");
    } catch (Exception e) {
      logger.error("websocket 异常");
    }
  }

  @OnClose
  public void onClose() {
    webSocketSessionManager.revoke(sid);
    logger.info("有一连接关闭！当前在线人数为 [ {} ] ", webSocketSessionManager.connectedCounts());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    logger.info("收到来自窗口 [ {} ] 的信息: {}", sid, message);

    // 群发消息
    webSocketSessionManager.broadcast(message);
  }

  @OnError
  public void onError(Session session, Throwable error) {
    logger.error("发生错误 id[{}] uri[{}] sid[{}]", session.getId(), session.getRequestURI(), this.sid);
    error.printStackTrace();
  }

  /**
   * 实现服务器主动推送
   */
  protected void sendMessage(String message) throws IOException {
    Map<String, String> msg = new HashMap<>(2);

    msg.put("timestamp", new Date().toString());
    msg.put("message", message);

    this.session.getBasicRemote().sendText(jsonHelper.toJSONString(msg));
  }
}

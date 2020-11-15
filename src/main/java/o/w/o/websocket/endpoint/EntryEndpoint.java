package o.w.o.websocket.endpoint;

import lombok.extern.log4j.Log4j2;
import o.w.o.server.definition.ApiException;
import o.w.o.websocket.WebSocketSession;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * WebSocketSession
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/1/21 17:39
 */
@Log4j2
@Component
@ServerEndpoint("/websocket/{sid}/{accessToken}")
public class EntryEndpoint extends WebSocketSession {

  @OnOpen
  public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("accessToken") String accessToken) {
    init();

    var result = this.getAuthenticationService().validateJwt(accessToken);
    if (result.isSuccess()) {
      var payload = result.guard();

      if (Objects.equals(sid, payload.getJwt().getUid().toString())) {
        session.getUserPrincipal();
      } else {
        try {
          CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "用户权限不匹配！");

          session.getBasicRemote().sendText(
              this.getJsonHelper().toJsonString(ApiException.forbidden("用户权限不匹配！"))
          );
          session.close(closeReason);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      try {
        CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "用户权限不足！");

        session.getBasicRemote().sendText(
            this.getJsonHelper().toJsonString(ApiException.forbidden("用户权限不足！"))
        );
        session.close(closeReason);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    this.setSession(session).setSid(sid);

    this.getWebSocketSessionManager().register(sid, this);
    logger.info("有新窗口开始监听: [ {} ] ,当前在线人数为 [ {} ]", sid, this.getWebSocketSessionManager().connectedCounts());

    try {
      this.sendMessage("连接成功");
    } catch (Exception e) {
      logger.error("websocket 异常");
    }
  }

  @OnClose
  public void onClose() {
    this.getWebSocketSessionManager().revoke(this.getSid());
    logger.info("有一连接关闭！当前在线人数为 [ {} ]", this.getWebSocketSessionManager().connectedCounts());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    logger.info("收到来自窗口 [ {} ] 的信息: {}", this.getSid(), message);

    // 群发消息
    this.getWebSocketSessionManager().broadcast(message);
  }

  @OnError
  public void onError(Session session, Throwable error) throws IOException {
    logger.error("发生错误 id[{}] uri[{}] sid[{}]", session.getId(), session.getRequestURI(), this.getSid());
    error.printStackTrace();
    session.getBasicRemote().sendText(this.getJsonHelper().toJsonString(ApiException.badRequest(error.getMessage())));
  }

  /**
   * 实现服务器主动推送
   */
  public void sendMessage(String message) throws IOException {
    this.init();

    Map<String, String> msg = new HashMap<>(2);

    msg.put("timestamp", new Date().toString());
    msg.put("message", message);

    this.getSession().getBasicRemote().sendText(this.getJsonHelper().toJsonString(msg));
  }
}

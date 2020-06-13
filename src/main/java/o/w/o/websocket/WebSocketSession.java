package o.w.o.websocket;

import o.w.o.resource.system.authorization.service.AuthorizedJwtStoreService;
import o.w.o.server.io.api.APIException;
import o.w.o.util.ContextHelper;
import o.w.o.server.io.json.JsonHelper;
import lombok.extern.log4j.Log4j2;
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
public class WebSocketSession extends ContextHelper.AbstractContext {
  private JsonHelper jsonHelper;
  private WebSocketSessionManager webSocketSessionManager = WebSocketSessionManager.getWebSocketSessionManager();
  private AuthorizedJwtStoreService authorizedJwtStoreService;

  /**
   * 与某个客户端的连接会话，需要通过它来给客户端发送数据。
   */
  private Session session;
  private String sid = "";

  public WebSocketSession() {
    init();
  }

  private void init() {
    if (this.isContextInitCompletely()) {
      if (this.getContextInitialStatus() && !this.getResourceInitialStatus()) {
        this.jsonHelper = this.getBean(JsonHelper.class);
        this.authorizedJwtStoreService = this.getBean(AuthorizedJwtStoreService.class);
        this.setResourceInitialStatus(true);
      }
    } else {
      ContextHelper.attachApplicationContextToInstance(this);
      this.setContextInitialStatus(true);
    }
  }


  @OnOpen
  public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("accessToken") String accessToken) {
    init();

    var result = this.authorizedJwtStoreService.validate(accessToken);
    if(result.getSuccess()) {
      var payload = result.guard();
      if(Objects.equals(sid, payload.getJwt().getUid().toString())) {
        session.getUserPrincipal();
      } else {
        try {
          CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "用户权限不匹配！");

          session.getBasicRemote().sendText(
              this.jsonHelper.toJsonString(APIException.forbidden("用户权限不匹配！"))
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
            this.jsonHelper.toJsonString(APIException.forbidden("用户权限不足！"))
        );
        session.close(closeReason);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

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
    logger.info("有一连接关闭！当前在线人数为 [ {} ]", webSocketSessionManager.connectedCounts());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    logger.info("收到来自窗口 [ {} ] 的信息: {}", sid, message);

    // 群发消息
    webSocketSessionManager.broadcast(message);
  }

  @OnError
  public void onError(Session session, Throwable error) throws IOException {
    logger.error("发生错误 id[{}] uri[{}] sid[{}]", session.getId(), session.getRequestURI(), this.sid);
    error.printStackTrace();
    session.getBasicRemote().sendText(jsonHelper.toJsonString(APIException.of(error.getMessage())));
  }

  /**
   * 实现服务器主动推送
   */
  public void sendMessage(String message) throws IOException {
    init();

    Map<String, String> msg = new HashMap<>(2);

    msg.put("timestamp", new Date().toString());
    msg.put("message", message);

    this.session.getBasicRemote().sendText(jsonHelper.toJsonString(msg));
  }
}

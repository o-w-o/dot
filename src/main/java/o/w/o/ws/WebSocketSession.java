package o.w.o.ws;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.authentication.service.AuthenticationService;
import o.w.o.util.ContextUtil;
import o.w.o.infrastructure.helper.JsonHelper;

import javax.websocket.Session;

/**
 * WebSocketSession
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/1/21 17:39
 */
@Slf4j
public class WebSocketSession extends ContextUtil.AbstractContext {
  @Getter
  private final WebSocketSessionManager webSocketSessionManager = WebSocketSessionManager.getWebSocketSessionManager();

  @Getter
  private JsonHelper jsonHelper;
  @Getter
  private AuthenticationService authenticationService;

  /**
   * 与某个客户端的连接会话，需要通过它来给客户端发送数据。
   */
  @Getter
  @Setter
  private Session session;
  @Getter
  @Setter
  private String sid = "";

  public WebSocketSession() {
    init();
  }

  protected void init() {
    if (this.isContextInitCompletely()) {
      if (this.getContextInitialStatus() && !this.getResourceInitialStatus()) {
        this.jsonHelper = this.getBean(JsonHelper.class);
        this.authenticationService = this.getBean(AuthenticationService.class);
        this.setResourceInitialStatus(true);
      }
    } else {
      ContextUtil.attachApplicationContextToInstance(this);
      this.setContextInitialStatus(true);
    }
  }
}

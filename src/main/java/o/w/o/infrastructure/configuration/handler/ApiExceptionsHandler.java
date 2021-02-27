package o.w.o.infrastructure.configuration.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一 JSON 格式返回
 *
 * @author symbols@dingtalk.com
 * @date 2020/9/6
 */
@Slf4j
public class ApiExceptionsHandler {
  public void recordException(String message, Exception e) {
    logger.error(message, e);
  }

  public void recordException(Exception e) {
    this.recordException(String.format("recordAPIException:[ %s ]", e.getClass().getSimpleName()), e);
  }
}

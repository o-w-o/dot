package o.w.o.server.configuration.handler.exceptions;


import lombok.extern.slf4j.Slf4j;
import o.w.o.server.definition.ApiException;
import o.w.o.server.definition.ApiExceptionEntity;
import o.w.o.server.definition.ApiExceptions;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制并统一处理异常类
 * - @ExceptionHandler 标注的方法优先级问题，它会找到异常的最近继承关系，也就是继承关系最浅的注解方法
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ZeroExceptionsHandler {

  private void recordException(String message, Exception e) {
    logger.error(message, e);
  }

  private void recordException(Exception e) {
    this.recordException(String.format("recordAPIException:[ %s ]", e.getClass().getSimpleName()), e);
  }

  /**
   * 未知异常
   */
  @ExceptionHandler(value = Exception.class)
  public ApiExceptionEntity unKnowExceptionHandler(HttpServletRequest request, Object handler, Exception e) {
    this.recordException(e);

    String message;
    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      message = String.format("未知异常，方法：%s.%s，异常摘要：%s",
          handlerMethod.getBean().getClass().getName(),
          handlerMethod.getMethod().getName(),
          e.getMessage());
    } else {
      message = ApiExceptions.internalServerError.getMessage();
    }

    return ApiExceptionEntity.of(ApiException.from(request).setMessage(message));
  }
}
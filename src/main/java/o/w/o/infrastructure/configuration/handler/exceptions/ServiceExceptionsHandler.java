package o.w.o.infrastructure.configuration.handler.exceptions;

import lombok.extern.slf4j.Slf4j;
import o.w.o.infrastructure.configuration.handler.ApiExceptionsHandler;
import o.w.o.infrastructure.definition.ApiException;
import o.w.o.infrastructure.definition.ApiExceptionEntity;
import o.w.o.infrastructure.definition.ServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ServiceExceptionsHandler extends ApiExceptionsHandler {

  /**
   * 接口异常
   */
  @ExceptionHandler(ApiException.class)
  public ApiExceptionEntity apiException(HttpServletRequest request, ApiException e) {
    return ApiExceptionEntity.of(e);
  }

  /**
   * 业务异常
   */
  @ExceptionHandler(ServiceException.class)
  public ApiExceptionEntity serviceException(HttpServletRequest request, ServiceException e) {
    return ApiExceptionEntity.of(ApiException.from(
        request,
        String.format("服务异常，%s", e.getResult().getMessage()),
        e.getResult().getCode(),
        false
    ));
  }
}

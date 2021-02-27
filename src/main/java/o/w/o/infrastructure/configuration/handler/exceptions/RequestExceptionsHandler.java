package o.w.o.infrastructure.configuration.handler.exceptions;


import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.infrastructure.configuration.handler.ApiExceptionsHandler;
import o.w.o.infrastructure.definition.ApiException;
import o.w.o.infrastructure.definition.ApiExceptionEntity;
import o.w.o.infrastructure.definition.ApiExceptions;
import o.w.o.infrastructure.util.ServiceUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

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
public class RequestExceptionsHandler extends ApiExceptionsHandler {

  /**
   * 401 认证异常
   */
  @ExceptionHandler(value = {
      AuthenticationException.class,
      AuthenticationCredentialsNotFoundException.class,
      JwtException.class,
  })
  public ApiExceptionEntity unauthorizedException(HttpServletRequest request) {
    return ApiExceptionEntity.of(ApiException.from(request, ApiExceptions.unauthorized));
  }

  /**
   * 403 无授权异常
   */
  @ExceptionHandler(value = {
      AccessDeniedException.class,
  })
  public ApiExceptionEntity forbiddenException(HttpServletRequest request) {
    var optPrincipal = ServiceUtil.fetchAuthentication().map(Authentication::getPrincipal);
    if (optPrincipal.isPresent()) {
      var authorizedUser = (AuthorizedUser) optPrincipal.get();
      if (authorizedUser.isAnonymous()) {
        return ApiExceptionEntity.of(ApiException.unauthorized());
      } else {
        return ApiExceptionEntity.of(ApiException.forbidden());
      }
    } else {
      return ApiExceptionEntity.of(ApiException.badRequest("禁止访问"));
    }
  }

  /**
   * 404 空白异常
   */
  @ExceptionHandler(value = {
      NoHandlerFoundException.class,
  })
  public ApiExceptionEntity notFoundException(HttpServletRequest request, NoHandlerFoundException e) {
    return ApiExceptionEntity.of(ApiException.from(request, ApiExceptions.notFound));
  }
}
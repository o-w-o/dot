package ink.o.w.o.server.controller;


import ink.o.w.o.server.constant.HttpExceptionStatus;
import ink.o.w.o.server.domain.ResponseEntityExceptionBody;
import ink.o.w.o.server.domain.ResponseEntityFactory;
import ink.o.w.o.server.exception.ServiceException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 控制并统一处理异常类
 * - @ExceptionHandler标注的方法优先级问题，它会找到异常的最近继承关系，也就是继承关系最浅的注解方法
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ExceptionsController {

  private void recordException(Exception e) {
    e.printStackTrace();
  }

  /**
   * 未知异常
   */
  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<?> unKnowExceptionHandler(HttpServletRequest request, Object handler, Exception e) {
    recordException(e);

    String message;
    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      message = String.format("未知异常，方法：%s.%s，异常摘要：%s",
          handlerMethod.getBean().getClass().getName(),
          handlerMethod.getMethod().getName(),
          e.getMessage());
    } else {
      message = HttpExceptionStatus.internalServerError.getMessage();
    }

    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, message),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * 401
   */
  @ExceptionHandler(value = {
      AuthenticationException.class,
      AuthenticationCredentialsNotFoundException.class,
      MissingClaimException.class,
      IncorrectClaimException.class,
      SignatureException.class,
      MalformedJwtException.class,
  })
  public ResponseEntity<?> unauthorizedException(HttpServletRequest request) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, HttpExceptionStatus.unauthorized),
        HttpStatus.UNAUTHORIZED
    );
  }

  /**
   * 403
   */
  @ExceptionHandler(value = {
      AccessDeniedException.class,
  })
  public ResponseEntity<?> forbiddenException(HttpServletRequest request) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, HttpExceptionStatus.forbidden),
        HttpStatus.FORBIDDEN
    );
  }

  /**
   * 404
   */
  @ExceptionHandler(value = {
      NoHandlerFoundException.class,
  })
  public ResponseEntity<?> notFoundException(HttpServletRequest request, NoHandlerFoundException e) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, HttpExceptionStatus.notFound),
        HttpStatus.NOT_FOUND
    );
  }

  /**
   * 运行时异常
   */
  @ExceptionHandler(value = RuntimeException.class)
  public ResponseEntity<?> runtimeExceptionHandler(HttpServletRequest request, RuntimeException e) {
    e.printStackTrace();

    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, HttpExceptionStatus.internalServerError),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * 空指针异常
   */
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<?> nullPointerExceptionHandler(HttpServletRequest request, NullPointerException e) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, HttpExceptionStatus.internalServerError),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * 类型转换异常
   */
  @ExceptionHandler(ClassCastException.class)
  public ResponseEntity<?> classCastExceptionHandler(HttpServletRequest request, ClassCastException e) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, HttpExceptionStatus.internalServerError),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * IO 异常
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<?> ioExceptionHandler(HttpServletRequest request, IOException e) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, HttpExceptionStatus.internalServerError),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * 数组越界异常
   */
  @ExceptionHandler(IndexOutOfBoundsException.class)
  public ResponseEntity<?> indexOutOfBoundsExceptionHandler(HttpServletRequest request, IndexOutOfBoundsException e) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, HttpExceptionStatus.internalServerError),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * 参数类型不匹配
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<?> requestTypeMismatch(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
    String message = String.format("参数类型不匹配，参数 [ %s ] 类型必须为 [ %s ]", e.getName(), e.getRequiredType());

    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, message),
        HttpStatus.BAD_REQUEST
    );
  }

  /**
   * 缺少参数
   */
  @ExceptionHandler({MissingServletRequestParameterException.class})
  public ResponseEntity<?> requestMissingServletRequest(HttpServletRequest request, MissingServletRequestParameterException e) {
    String message = String.format("缺少必要参数 [ %s ]", e.getParameterName());

    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, message),
        HttpStatus.BAD_REQUEST
    );
  }

  /**
   * 请求 Method 不匹配
   */
  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<?> requestMissingServletRequest(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
    String message = String.format("不支持 [ %s ] 方法，仅支持 [ %s ] 方法类型", e.getMethod(), StringUtils.join(e.getSupportedMethods(), ","));

    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, message),
        HttpStatus.BAD_REQUEST
    );
  }

  /**
   * RequestBody 数据类型转换异常
   */
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<?> httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
    AtomicReference<String> message = new AtomicReference<>(e.getMessage());

    HttpExceptionStatus.requiredRequestBodyMissing.getMessage(message.get()).ifPresent(message::set);

    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, message.get()),
        HttpStatus.BAD_REQUEST
    );
  }

  /**
   * 类方法参数异常
   */
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<?> methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
    var message = new AtomicReference<>("");

    Optional.ofNullable(e.getBindingResult().getFieldError()).ifPresentOrElse(fieldError -> message.set(
        fieldError.getDefaultMessage()),
        () -> message.set("fetch error failed！")
    );

    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, message.get()),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * 如果代理异常调用方法将会抛出此异常
   */
  @ExceptionHandler(UndeclaredThrowableException.class)
  public ResponseEntity<?> undeclaredThrowableException(HttpServletRequest request, UndeclaredThrowableException e) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(request, String.format("异常 -> [ %s ]", UndeclaredThrowableException.class.getSimpleName())),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  @ExceptionHandler(RequestRejectedException.class)
  public ResponseEntity<?> reject(RequestRejectedException e) {
    return ResponseEntityFactory.error(e.getMessage());
  }

  /**
   * 业务异常处理类
   */
  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<?> serviceException(HttpServletRequest request, ServiceException e) {
    return ResponseEntityFactory.generateFrom(
        ResponseEntityExceptionBody.of(
            request,
            String.format("服务异常，%s", e.getResult().getMessage()),
            e.getResult().getCode(),
            false
        ),
        HttpStatus.BAD_REQUEST
    );
  }
}
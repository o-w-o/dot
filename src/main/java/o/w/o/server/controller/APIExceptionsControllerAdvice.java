package o.w.o.server.controller;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIExceptionEntity;
import o.w.o.server.io.api.APIExceptions;
import o.w.o.server.io.json.JsonParseEntityEnumException;
import o.w.o.server.io.service.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
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
public class APIExceptionsControllerAdvice {

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
  public APIExceptionEntity unKnowExceptionHandler(HttpServletRequest request, Object handler, Exception e) {
    this.recordException(e);

    String message;
    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      message = String.format("未知异常，方法：%s.%s，异常摘要：%s",
          handlerMethod.getBean().getClass().getName(),
          handlerMethod.getMethod().getName(),
          e.getMessage());
    } else {
      message = APIExceptions.internalServerError.getMessage();
    }

    return APIExceptionEntity.of(APIException.from(request, message));
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
  public APIExceptionEntity unauthorizedException(HttpServletRequest request) {
    return APIExceptionEntity.of(APIException.from(request, APIExceptions.unauthorized));
  }

  /**
   * 403
   */
  @ExceptionHandler(value = {
      AccessDeniedException.class,
  })
  public APIExceptionEntity forbiddenException(HttpServletRequest request) {
    return APIExceptionEntity.of(APIException.from(request, APIExceptions.forbidden));
  }

  /**
   * 404
   */
  @ExceptionHandler(value = {
      NoHandlerFoundException.class,
  })
  public APIExceptionEntity notFoundException(HttpServletRequest request, NoHandlerFoundException e) {
    return APIExceptionEntity.of(APIException.from(request, APIExceptions.notFound));
  }

  /**
   * 运行时异常
   */
  @ExceptionHandler(value = RuntimeException.class)
  public APIExceptionEntity runtimeExceptionHandler(HttpServletRequest request, RuntimeException e) {
    this.recordException(e);
    return APIExceptionEntity.of(APIException.from(request, APIExceptions.internalServerError));
  }

  /**
   * 空指针异常
   */
  @ExceptionHandler(NullPointerException.class)
  public APIExceptionEntity nullPointerExceptionHandler(HttpServletRequest request, NullPointerException e) {
    this.recordException(e);
    return APIExceptionEntity.of(APIException.from(request, APIExceptions.internalServerError));
  }

  /**
   * 类型转换异常
   */
  @ExceptionHandler(ClassCastException.class)
  public APIExceptionEntity classCastExceptionHandler(HttpServletRequest request, ClassCastException e) {
    return APIExceptionEntity.of(APIException.from(request, APIExceptions.internalServerError));
  }

  /**
   * IO 异常
   */
  @ExceptionHandler(IOException.class)
  public APIExceptionEntity ioExceptionHandler(HttpServletRequest request, IOException e) {
    return APIExceptionEntity.of(APIException.from(request, APIExceptions.internalServerError));
  }

  /**
   * 数组越界异常
   */
  @ExceptionHandler(IndexOutOfBoundsException.class)
  public APIExceptionEntity indexOutOfBoundsExceptionHandler(HttpServletRequest request, IndexOutOfBoundsException e) {
    return APIExceptionEntity.of(APIException.from(request, APIExceptions.internalServerError));
  }

  /**
   * 参数类型不匹配
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public APIExceptionEntity requestTypeMismatch(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
    String message = String.format("参数类型不匹配，参数 [ %s ] 类型必须为 [ %s ]", e.getName(), e.getRequiredType());

    return APIExceptionEntity.of(APIException.from(request, message));
  }

  /**
   * 缺少参数
   *
   * @return ResponseEntity<APIException>
   */
  @ExceptionHandler({MissingServletRequestParameterException.class})
  public APIExceptionEntity requestMissingServletRequest(HttpServletRequest request, MissingServletRequestParameterException e) {
    String message = String.format("缺少必要参数 [ %s ]", e.getParameterName());

    return APIExceptionEntity.of(APIException.from(request, message));
  }

  /**
   * 请求 Method 不匹配
   */
  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public APIExceptionEntity httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
    String message = String.format("不支持 [ %s ] 方法，仅支持 [ %s ] 方法类型", e.getMethod(), Joiner.on(',').skipNulls().join(e.getSupportedMethods()));

    return APIExceptionEntity.of(APIException.from(request, message));
  }

  /**
   * RequestBody 数据类型转换异常
   */
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public APIExceptionEntity httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
    var rootCause = Throwables.getRootCause(e);

    if (rootCause instanceof JsonParseEntityEnumException) {
      return this.jsonParseEntityEnumException(request, (JsonParseEntityEnumException) rootCause);
    }

    if (rootCause instanceof InvalidFormatException) {
      return this.invalidFormatException(request, (InvalidFormatException) rootCause);
    }

    if (rootCause instanceof HttpMessageNotReadableException) {
      return APIExceptionEntity.of(APIException.from(request, this.httpMessageNotReadableException(e)));
    }

    return APIExceptionEntity.of(APIException.from(request, String.format("未知异常 [%s]，异常摘要：%s", e.getClass(), e.getMessage())));
  }

  private String httpMessageNotReadableException(HttpMessageNotReadableException e) {
    AtomicReference<String> message = new AtomicReference<>(e.getMessage());
    APIExceptions.requiredRequestBodyMissing.getMessage(message.get()).ifPresent(message::set);
    return message.get();
  }

  @ExceptionHandler({InvalidFormatException.class})
  public APIExceptionEntity invalidFormatException(HttpServletRequest request, InvalidFormatException e) {
    return APIExceptionEntity.of(APIException.from(request, String.format("数据转化失败！ [%s] -> [%s]", e.getProcessor(), e.getMessage())));

  }

  @ExceptionHandler({MissingRequestHeaderException.class})
  public APIExceptionEntity missingRequestHeaderException(HttpServletRequest request, MissingRequestHeaderException e) {
    return APIExceptionEntity.of(APIException.from(
        request,
        String.format("%s -> %s", APIExceptions.requiredRequestHeaderMissing.getMessage(), e.getHeaderName())
    ));
  }

  /**
   * 类方法参数异常
   */
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public APIExceptionEntity methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
    var message = new AtomicReference<>(new HashMap<String, String>());

    e.getBindingResult().getFieldErrors()
        .forEach(fieldError ->
            message.get().put(fieldError.getField(), fieldError.getDefaultMessage())
        );


    return APIExceptionEntity.of(APIException.from(request, "参数校验失败！").setPayload(message.get()));
  }

  /**
   * JSON 数据转换异常
   */
  @ExceptionHandler({JsonMappingException.class})
  public APIExceptionEntity jsonMappingException(HttpServletRequest request, JsonMappingException e) {
    return APIExceptionEntity.of(APIException
        .from(request, String.format("数据转化失败！ [%s]", e.getOriginalMessage())));
  }

  /**
   * JSON 数据转换异常
   */
  @ExceptionHandler({JsonParseEntityEnumException.class})
  public APIExceptionEntity jsonParseEntityEnumException(HttpServletRequest request, JsonParseEntityEnumException e) {
    var payload = new HashMap<String, Object>();
    payload.put("enum", e.enumClazz.getEnumConstants());

    return APIExceptionEntity.of(APIException.from(
        request,
        String.format("数据枚举转化失败！ [%s]", e.getEnumName())
    ).setPayload(
        payload
    ).setCode(JsonParseEntityEnumException.exceptionCode));
  }

  /**
   * 如果代理异常调用方法将会抛出此异常
   */
  @ExceptionHandler(UndeclaredThrowableException.class)
  public APIExceptionEntity undeclaredThrowableException(HttpServletRequest request, UndeclaredThrowableException e) {
    return APIExceptionEntity.of(APIException.from(request, String.format("异常 -> [ %s ]", UndeclaredThrowableException.class.getSimpleName()))
        .setCode(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public APIExceptionEntity httpClient(HttpClientErrorException e) {
    return APIExceptionEntity.of(APIException.of(e.getMessage(), e.getStatusCode()));
  }


  @ExceptionHandler(RequestRejectedException.class)
  public APIExceptionEntity reject(RequestRejectedException e) {
    return APIExceptionEntity.of(APIException.of(e.getMessage()));
  }


  /**
   * 业务异常处理类
   */
  @ExceptionHandler(ServiceException.class)
  public APIExceptionEntity serviceException(HttpServletRequest request, ServiceException e) {
    return APIExceptionEntity.of(APIException.from(
        request,
        String.format("服务异常，%s", e.getResult().getMessage()),
        e.getResult().getCode(),
        false
    ));
  }
}
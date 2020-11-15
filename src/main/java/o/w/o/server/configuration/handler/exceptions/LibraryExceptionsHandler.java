package o.w.o.server.configuration.handler.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import o.w.o.server.configuration.handler.ApiExceptionsHandler;
import o.w.o.server.definition.ApiException;
import o.w.o.server.definition.ApiExceptionEntity;
import o.w.o.server.definition.ApiExceptions;
import o.w.o.server.definition.EnumNotExistItemException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * LibraryExceptionsHandler
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
@Slf4j
@RestControllerAdvice
public class LibraryExceptionsHandler extends ApiExceptionsHandler {

  /**
   * JSON 数据转换异常
   */
  @ExceptionHandler({JsonMappingException.class})
  public ApiExceptionEntity jsonMappingException(HttpServletRequest request, JsonMappingException e) {
    return ApiExceptionEntity.of(ApiException
        .from(request).setMessage(String.format("数据转化失败！ [%s]", e.getOriginalMessage())));
  }

  /**
   * JSON 枚举转换异常
   */
  @ExceptionHandler({EnumNotExistItemException.class})
  public ApiExceptionEntity jsonParseEntityEnumException(HttpServletRequest request, EnumNotExistItemException e) {
    var payload = new HashMap<String, Object>();
    payload.put("enum", e.enumClazz.getEnumConstants());

    return ApiExceptionEntity.of(ApiException.from(request).setMessage(String.format("数据枚举转化失败！ [%s]", e.getEnumName())
    ).setPayload(
        payload
    ).setCode(EnumNotExistItemException.EXCEPTION_CODE));
  }

  /**
   * RequestBody 数据类型转换异常
   */
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ApiExceptionEntity httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
    var rootCause = Throwables.getRootCause(e);

    if (rootCause instanceof EnumNotExistItemException) {
      return this.jsonParseEntityEnumException(request, (EnumNotExistItemException) rootCause);
    }

    if (rootCause instanceof InvalidFormatException) {
      return this.invalidFormatException(request, (InvalidFormatException) rootCause);
    }

    if (rootCause instanceof HttpMessageNotReadableException) {
      return ApiExceptionEntity.of(ApiException.from(request).setMessage(this.httpMessageNotReadableException(e)));
    }

    return ApiExceptionEntity.of(ApiException.from(request).setMessage(String.format("未知异常 [%s]，异常摘要：%s", e.getClass(), e.getMessage())));
  }

  private String httpMessageNotReadableException(HttpMessageNotReadableException e) {
    AtomicReference<String> message = new AtomicReference<>(e.getMessage());
    ApiExceptions.requiredRequestBodyMissing.getMessage(message.get()).ifPresent(message::set);
    return message.get();
  }

  @ExceptionHandler({InvalidFormatException.class})
  public ApiExceptionEntity invalidFormatException(HttpServletRequest request, InvalidFormatException e) {
    return ApiExceptionEntity.of(ApiException.from(request).setMessage(String.format("数据转化失败！ [%s] -> [%s]", e.getProcessor(), e.getMessage())));
  }
}

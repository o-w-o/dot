package o.w.o.infrastructure.configuration.handler.exceptions;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import o.w.o.infrastructure.configuration.handler.ApiExceptionsHandler;
import o.w.o.infrastructure.definition.ApiException;
import o.w.o.infrastructure.definition.ApiExceptionEntity;
import o.w.o.infrastructure.definition.ApiExceptions;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Objects;

/**
 * FrameworkExceptionsHandler
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
@Slf4j
@RestControllerAdvice
public class FrameworkExceptionsHandler extends ApiExceptionsHandler {
  /**
   * 参数类型不匹配
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ApiExceptionEntity requestTypeMismatch(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
    String message = String.format("参数类型不匹配，参数 [ %s ] 类型必须为 [ %s ]", e.getName(), e.getRequiredType());

    return ApiExceptionEntity.of(ApiException.from(request).setMessage(message));
  }

  /**
   * 参数缺少
   *
   * @return ResponseEntity<APIException>
   */
  @ExceptionHandler({MissingServletRequestParameterException.class})
  public ApiExceptionEntity requestMissingServletRequest(HttpServletRequest request, MissingServletRequestParameterException e) {
    String message = String.format("缺少必要参数 [ %s ]", e.getParameterName());

    return ApiExceptionEntity.of(ApiException.from(request).setMessage(message));
  }

  /**
   * 请求方法不支持
   */
  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public ApiExceptionEntity httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
    String message = String.format("不支持 [ %s ] 方法，仅支持 [ %s ] 方法类型", e.getMethod(), Joiner.on(',').skipNulls().join(Objects.requireNonNull(e.getSupportedMethods())));

    return ApiExceptionEntity.of(ApiException.from(request).setMessage(message));
  }

  /**
   * 类方法参数异常
   */
  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ApiExceptionEntity methodArgumentNotValidException(HttpServletRequest request, Exception e) {
    var payload = new HashMap<String, String>(2);

    if (e instanceof MethodArgumentNotValidException) {
      ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()
          .forEach(fieldError ->
              payload.put(fieldError.getField(), fieldError.getDefaultMessage())
          );
    }

    if (e instanceof BindException) {
      ((BindException) e).getBindingResult().getFieldErrors()
          .forEach(fieldError ->
              payload.put(fieldError.getField(), fieldError.getDefaultMessage())
          );
    }

    return ApiExceptionEntity.of(ApiException.from(request).setMessage("参数校验失败！").setPayload(payload));
  }

  /**
   * 违反约束
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ApiExceptionEntity constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException e) {
    var payload = new HashMap<String, String>(2);

    e.getConstraintViolations().forEach(
        v -> payload.put(v.getPropertyPath().toString(), v.getMessage())
    );

    return ApiExceptionEntity.of(ApiException.from(request).setMessage("参数校验失败！").setPayload(payload));
  }

  /**
   * 请求头缺失
   */
  @ExceptionHandler({MissingRequestHeaderException.class})
  public ApiExceptionEntity missingRequestHeaderException(HttpServletRequest request, MissingRequestHeaderException e) {
    return ApiExceptionEntity.of(ApiException.from(request)
        .setMessage(
            String.format("%s -> %s", ApiExceptions.requiredRequestHeaderMissing.getMessage(), e.getHeaderName())
        ));
  }

  /**
   * 事务异常
   *
   * @author symbols@dingtalk.com
   * @date 2020/11/20
   */
  @ExceptionHandler(TransactionSystemException.class)
  public ApiExceptionEntity transactionSystemExceptionHandler(HttpServletRequest request, TransactionSystemException e) {
    var rootCause = Throwables.getRootCause(e);

    if (rootCause instanceof ConstraintViolationException) {
      return this.constraintViolationExceptionHandler(request, (ConstraintViolationException) rootCause);
    }

    if (rootCause instanceof RollbackException) {
      return ApiExceptionEntity.of(ApiException.badRequest(rootCause.getMessage()));
    }

    if (rootCause instanceof TransactionSystemException) {
      return ApiExceptionEntity.of(ApiException.from(request));
    }

    return ApiExceptionEntity.of(ApiException.badRequest(rootCause.getMessage()));
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public ApiExceptionEntity httpClient(HttpClientErrorException e) {
    return ApiExceptionEntity.of(ApiException.of(e.getMessage(), e.getStatusCode()));
  }


  @ExceptionHandler(RequestRejectedException.class)
  public ApiExceptionEntity reject(RequestRejectedException e) {
    return ApiExceptionEntity.of(ApiException.badRequest(e.getMessage()));
  }
}

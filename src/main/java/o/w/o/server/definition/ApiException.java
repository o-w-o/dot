package o.w.o.server.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import o.w.o.util.HttpUtil;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

/**
 * APIException
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/30
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
public class ApiException extends RuntimeException implements Supplier<ApiException> {
  public static final String DEFAULT_MESSAGE = HttpStatus.BAD_REQUEST.getReasonPhrase();
  public static final Integer DEFAULT_CODE = HttpStatus.BAD_REQUEST.value();
  private final Boolean succeed = false;

  private Integer code;
  private String method = "";
  private String path = "";
  private Map<String, ?> payload;
  private String message;
  private Date timestamp;

  public ApiException() {
    this.code = DEFAULT_CODE;
    this.message = DEFAULT_MESSAGE;
    this.timestamp = new Date(System.currentTimeMillis());
  }

  public static ApiException of(String message, Integer status) {
    return new ApiException()
        .setCode(status)
        .setMessage(message);
  }

  public static ApiException of(String message, HttpStatus status) {
    return of(message, status.value());
  }

  public static ApiException badRequest() {
    return of(ApiExceptions.badRequest.getMessage(), HttpStatus.BAD_REQUEST);
  }

  public static ApiException badRequest(String message) {
    return of(message, HttpStatus.BAD_REQUEST);
  }

  public static ApiException unauthorized(String message) {
    return of(message, HttpStatus.UNAUTHORIZED);
  }

  public static ApiException unauthorized() {
    return unauthorized(HttpStatus.UNAUTHORIZED.getReasonPhrase());
  }

  public static ApiException forbidden(String message) {
    return of(message, HttpStatus.FORBIDDEN);
  }

  public static ApiException forbidden() {
    return forbidden(HttpStatus.FORBIDDEN.getReasonPhrase());
  }

  public static ApiException from(HttpServletRequest request, String message, Integer code, Boolean format) {
    return new ApiException()
        .setPath(request.getRequestURI())
        .setMethod(request.getMethod().toUpperCase())
        .setCode(code)
        .setMessage(
            format ? HttpUtil.formatResponseExceptionMessage(request).apply(message) : message
        );
  }

  public static ApiException from(HttpServletRequest request, String message, Integer code) {
    return from(request, message, code, true);
  }

  public static ApiException from(HttpServletRequest request, HttpStatus status) {
    return from(
        request,
        status.getReasonPhrase(),
        status.value()
    );
  }

  public static ApiException from(HttpServletRequest request, ApiExceptions status) {
    return from(
        request,
        status.getMessage(),
        status.getCode()
    );
  }

  public static ApiException from(HttpServletRequest request) {
    return from(
        request,
        ApiExceptions.badRequest.getMessage(),
        ApiExceptions.badRequest.getCode()
    );
  }

  @Override
  public ApiException get() {
    return this;
  }

  @Builder
  public static class Exception {
    @Singular("error")
    private Map<String, Object> errors;
  }
}

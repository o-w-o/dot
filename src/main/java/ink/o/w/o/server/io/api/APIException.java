package ink.o.w.o.server.io.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ink.o.w.o.util.HttpHelper;
import lombok.*;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
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
@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
public class APIException extends RuntimeException implements Supplier<APIException> {
  public static final String DEFAULT_MESSAGE = "[default]: 接口异常";
  public static final Integer DEFAULT_CODE = 110;

  private Integer code;
  private String method = "";
  private String path = "";
  private Map<String, ?> payload;
  private String message;
  private Date timestamp = new Date();

  public APIException() {
    this.code = DEFAULT_CODE;
    this.message = DEFAULT_MESSAGE;
  }

  public static String fetchMessage(Integer code) {
    return Arrays.stream(APIExceptions.values())
        .filter(apiExceptions -> apiExceptions.getCode().equals(code))
        .findFirst()
        .map(APIExceptions::getMessage)
        .orElse(DEFAULT_MESSAGE);
  }

  public static APIException of(Integer code) {
    return of(fetchMessage(code), code);
  }

  public static APIException of() {
    return of("", HttpStatus.BAD_REQUEST);
  }

  public static APIException of(String message) {
    return of(message, HttpStatus.BAD_REQUEST);
  }

  public static APIException of(String message, Integer status) {
    return new APIException()
        .setCode(status)
        .setMessage(message);
  }

  public static APIException of(String message, HttpStatus status) {
    return new APIException()
        .setCode(status.value())
        .setMessage(message);
  }

  public static APIException unauthorized(String message) {
    return of(message, HttpStatus.UNAUTHORIZED);
  }

  public static APIException unauthorized() {
    return unauthorized(APIExceptions.unauthorized.getMessage());
  }

  public static APIException forbidden(String message) {
    return of(message, HttpStatus.FORBIDDEN);
  }

  public static APIException forbidden() {
    return forbidden(APIExceptions.forbidden.getMessage());
  }

  public static APIException from(HttpServletRequest request, String message, Integer code, Boolean appendApiPath) {
    return new APIException()
        .setPath(request.getRequestURI())
        .setMethod(request.getMethod().toUpperCase())
        .setCode(code)
        .setMessage(
            appendApiPath ? HttpHelper.formatResponseDataMessage(request).apply(message) : message
        );
  }

  public static APIException from(HttpServletRequest request, String message, Integer code) {
    return new APIException()
        .setPath(request.getRequestURI())
        .setMethod(request.getMethod().toUpperCase())
        .setCode(code)
        .setMessage(
            HttpHelper.formatResponseDataMessage(request).apply(message)
        );
  }

  public static APIException from(HttpServletRequest request, HttpStatus status) {
    return from(
        request,
        status.getReasonPhrase(),
        status.value()
    );
  }

  public static APIException from(HttpServletRequest request, APIExceptions status) {
    return from(
        request,
        status.getMessage(),
        status.getCode()
    );
  }

  public static APIException from(HttpServletRequest request, Integer code) {
    return from(request, fetchMessage(code), code);
  }

  public static APIException from(HttpServletRequest request, String message) {
    return from(request, message, APIExceptions.badRequest.getCode());
  }

  public static APIException from(HttpServletRequest request) {
    return from(
        request,
        APIExceptions.badRequest.getMessage(),
        APIExceptions.badRequest.getCode()
    );
  }

  public APIException setCode(Integer code) {
    this.code = code;
    return this;
  }

  public APIException setCode(HttpStatus httpStatus) {
    this.code = httpStatus.value();
    return this;
  }

  @Override
  public APIException get() {
    return this;
  }

  @Data
  @Builder
  public static class Exception {
    @Singular("error")
    Map<String, Object> errors;
  }
}

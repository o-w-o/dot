package ink.o.w.o.server.domain;

import ink.o.w.o.server.constant.HttpExceptionStatus;
import ink.o.w.o.util.HttpHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * API 结果
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/4 下午6:56
 */
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntityExceptionBody<T> {

  @Getter
  private Integer code;

  @Getter
  @Setter
  private String message = "";

  @Getter
  @Setter
  private String method = "";

  @Getter
  @Setter
  private String path = "";

  @Getter
  @Setter
  private T payload;

  @Getter
  @Setter
  private Date timestamp;

  public static String ofExceptionCodeMessage(Integer code) {
    return Arrays.stream(HttpExceptionStatus.values())
        .filter(httpExceptionStatus -> httpExceptionStatus.getCode().equals(code))
        .findFirst()
        .map(HttpExceptionStatus::getMessage)
        .orElse("");
  }

  public static <T> ResponseEntityExceptionBody<T> error(String message) {
    return error(message, HttpStatus.BAD_REQUEST);
  }

  public static <T> ResponseEntityExceptionBody<T> error(String message, Integer status) {
    return new ResponseEntityExceptionBody<T>()
        .setCode(status)
        .setMessage(message);
  }

  public static <T> ResponseEntityExceptionBody<T> error(String message, HttpStatus status) {
    return new ResponseEntityExceptionBody<T>()
        .setCode(status)
        .setMessage(message);
  }

  public static <T> ResponseEntityExceptionBody<T> unauthorized(String message) {
    return error(message, HttpStatus.UNAUTHORIZED);
  }

  public static <T> ResponseEntityExceptionBody<T> unauthorized() {
    return unauthorized(HttpExceptionStatus.unauthorized.getMessage());
  }

  public static <T> ResponseEntityExceptionBody<T> forbidden(String message) {
    return error(HttpExceptionStatus.forbidden.getMessage(), HttpStatus.FORBIDDEN);
  }

  public static <T> ResponseEntityExceptionBody<T> forbidden() {
    return forbidden(HttpExceptionStatus.forbidden.getMessage());
  }

  public static <T> ResponseEntityExceptionBody<T> of(HttpServletRequest request, String message, Integer code) {
    return new ResponseEntityExceptionBody<T>()
        .setPath(request.getRequestURI())
        .setMethod(request.getMethod().toUpperCase())
        .setCode(code)
        .setMessage(
            HttpHelper.formatResponseDataMessage(request).apply(message)
        );
  }

  public static <T> ResponseEntityExceptionBody<T> of(HttpServletRequest request, HttpStatus status) {
    return of(
        request,
        status.getReasonPhrase(),
        status.value()
    );
  }

  public static <T> ResponseEntityExceptionBody<T> of(HttpServletRequest request, HttpExceptionStatus status) {
    return of(
        request,
        status.getMessage(),
        status.getCode()
    );
  }

  public static <T> ResponseEntityExceptionBody<T> of(HttpServletRequest request, Integer code) {
    return of(request, ofExceptionCodeMessage(code), code);
  }

  public static <T> ResponseEntityExceptionBody<T> of(HttpServletRequest request, String message) {
    return of(request, message, HttpExceptionStatus.badRequest.getCode());
  }

  public static <T> ResponseEntityExceptionBody<T> of(HttpServletRequest request) {
    return of(
        request,
        HttpExceptionStatus.badRequest.getMessage(),
        HttpExceptionStatus.badRequest.getCode()
    );
  }

  public ResponseEntityExceptionBody<T> setCode(int code) {
    this.code = code;
    this.autocomplete();
    return this;
  }

  public ResponseEntityExceptionBody<T> setCode(HttpStatus code) {
    this.code = code.value();
    this.autocomplete();
    return this;
  }

  private void autocomplete() {
    if (this.payload == null) {
      this.payload = (T) new Object();
    }
    if (this.timestamp == null) {
      this.timestamp = new Date();
    }
  }
}

package ink.o.w.o.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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

  public final static String UNAUTHORIZED_DEFAULT_MESSAGE = "未授权的访问！";
  public final static String FORBIDDEN_DEFAULT_MESSAGE = "授权不足！";

  @Getter
  private Integer code;

  @Getter
  @Setter
  private String message = "";

  @Getter
  @Setter
  private String path = "";

  @Getter
  @Setter
  private T payload;

  @Getter
  @Setter
  private Date timestamp;

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
    return error(ResponseEntityExceptionBody.UNAUTHORIZED_DEFAULT_MESSAGE, HttpStatus.UNAUTHORIZED);
  }

  public static <T> ResponseEntityExceptionBody<T> unauthorized() {
    return unauthorized(ResponseEntityExceptionBody.UNAUTHORIZED_DEFAULT_MESSAGE);
  }

  public static <T> ResponseEntityExceptionBody<T> forbidden(String message) {
    return error(ResponseEntityExceptionBody.FORBIDDEN_DEFAULT_MESSAGE, HttpStatus.FORBIDDEN);
  }

  public static <T> ResponseEntityExceptionBody<T> forbidden() {
    return forbidden(ResponseEntityExceptionBody.FORBIDDEN_DEFAULT_MESSAGE);
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

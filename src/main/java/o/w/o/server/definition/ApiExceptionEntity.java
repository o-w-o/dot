package o.w.o.server.definition;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;
/**
 * ApiExceptionEntity
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/20
 */
public class ApiExceptionEntity extends ResponseEntity<ApiException> {
  public ApiExceptionEntity(ApiException apiException) {
    this(apiException, Stream.of(HttpStatus.values()).filter(httpStatus -> httpStatus.value() == apiException.getCode()).findFirst().orElse(HttpStatus.BAD_REQUEST));
  }

  public ApiExceptionEntity(ApiException apiException, HttpStatus httpStatus) {
    super(apiException, httpStatus);
  }

  public static ApiExceptionEntity of(ApiException apiException) {
    return new ApiExceptionEntity(apiException);
  }

  public static ApiExceptionEntity of(ApiException apiException, HttpStatus httpStatus) {
    return new ApiExceptionEntity(apiException, httpStatus);
  }
}

package o.w.o.server.io.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

public class APIExceptionEntity extends ResponseEntity<APIException> {
  public APIExceptionEntity(APIException apiException) {
    this(apiException, Stream.of(HttpStatus.values()).filter(httpStatus -> httpStatus.value() == apiException.getCode()).findFirst().orElse(HttpStatus.BAD_REQUEST));
  }

  public APIExceptionEntity(APIException apiException, HttpStatus httpStatus) {
    super(apiException, httpStatus);
  }

  public static APIExceptionEntity of(APIException apiException) {
    return new APIExceptionEntity(apiException);
  }

  public static APIExceptionEntity of(APIException apiException, HttpStatus httpStatus) {
    return new APIExceptionEntity(apiException, httpStatus);
  }
}

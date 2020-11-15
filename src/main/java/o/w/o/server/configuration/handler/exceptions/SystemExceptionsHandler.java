package o.w.o.server.configuration.handler.exceptions;

import lombok.extern.slf4j.Slf4j;
import o.w.o.server.configuration.handler.ApiExceptionsHandler;
import o.w.o.server.definition.ApiException;
import o.w.o.server.definition.ApiExceptionEntity;
import o.w.o.server.definition.ApiExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

@Slf4j
@RestControllerAdvice
public class SystemExceptionsHandler extends ApiExceptionsHandler {

  /**
   * 运行时异常
   */
  @ExceptionHandler(value = RuntimeException.class)
  public ApiExceptionEntity runtimeExceptionHandler(HttpServletRequest request, RuntimeException e) {
    this.recordException(e);
    return ApiExceptionEntity.of(ApiException.from(request, ApiExceptions.internalServerError));
  }

  /**
   * 空指针异常
   */
  @ExceptionHandler(NullPointerException.class)
  public ApiExceptionEntity nullPointerExceptionHandler(HttpServletRequest request, NullPointerException e) {
    this.recordException(e);
    return ApiExceptionEntity.of(ApiException.from(request));
  }

  /**
   * 类型转换异常
   */
  @ExceptionHandler(ClassCastException.class)
  public ApiExceptionEntity classCastExceptionHandler(HttpServletRequest request, ClassCastException e) {
    return ApiExceptionEntity.of(ApiException.from(request, ApiExceptions.internalServerError).setMessage("类型转化异常"));
  }

  /**
   * IO 异常
   */
  @ExceptionHandler(IOException.class)
  public ApiExceptionEntity ioExceptionHandler(HttpServletRequest request, IOException e) {
    return ApiExceptionEntity.of(ApiException.from(request, ApiExceptions.internalServerError));
  }

  /**
   * 数组越界异常
   */
  @ExceptionHandler(IndexOutOfBoundsException.class)
  public ApiExceptionEntity indexOutOfBoundsExceptionHandler(HttpServletRequest request, IndexOutOfBoundsException e) {
    return ApiExceptionEntity.of(ApiException.from(request, ApiExceptions.internalServerError));
  }

  /**
   * 如果代理异常调用方法将会抛出此异常
   */
  @ExceptionHandler(UndeclaredThrowableException.class)
  public ApiExceptionEntity undeclaredThrowableException(HttpServletRequest request, UndeclaredThrowableException e) {
    return ApiExceptionEntity.of(
        ApiException
            .from(request)
            .setMessage(String.format("异常 -> [ %s ]", UndeclaredThrowableException.class.getSimpleName()))
            .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
    );
  }
}

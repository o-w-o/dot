package o.w.o.infrastructure.definition;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * 业务异常类, 默认错误码 400
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/4 上午11:12
 */
public class ServiceException extends RuntimeException implements Supplier<ServiceException> {

  @Getter
  @Setter
  private ServiceResult<?> result;

  public ServiceException(String message) {
    super(message);
    this.result = ServiceResult.error(message);
  }

  public ServiceException(String message, Integer code) {
    super(message);
    this.result = ServiceResult.error(message, code);
  }

  public ServiceException(ServiceResult<?> result) {
    super(result.getMessage());
    this.result = result;
  }

  public ServiceException(ServiceResult<?> result, Throwable cause) {
    super(result.getMessage(), cause);
    this.result = result;
  }

  public static ServiceException of(String message) {
    return new ServiceException(message);
  }

  public static ServiceException of(String message, Integer code) {
    return new ServiceException(message, code);
  }

  public static ServiceException unsupported() {
    return new ServiceException("不支持或未实现的方法");
  }

  public static ServiceException internalException(String s) {
    return new ServiceException(String.format("内部异常 [%s]", s));
  }

  @Override
  public ServiceException get() {
    return this;
  }
}

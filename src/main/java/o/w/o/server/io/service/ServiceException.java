package o.w.o.server.io.service;

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
  private ServiceResult<? extends Object> result;

  public ServiceException(String message) {
    super(message);
    this.result = ServiceResult.error(message);
  }

  public ServiceException(String message, Integer code) {
    super(message);
    this.result = ServiceResult.error(message, code);
  }

  public ServiceException(ServiceResult<? extends Object> result) {
    super(result.getMessage());
    this.result = result;
  }

  public ServiceException(ServiceResult<? extends Object> result, Throwable cause) {
    super(result.getMessage(), cause);
    this.result = result;
  }

  public static ServiceException of(String message) {
    return new ServiceException(message);
  }

  public static ServiceException unsupport() {
    return new ServiceException("不支持或未实现的方法");
  }

  @Override
  public ServiceException get() {
    return this;
  }
}

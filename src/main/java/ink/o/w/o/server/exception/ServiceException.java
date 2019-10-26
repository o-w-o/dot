package ink.o.w.o.server.exception;

import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
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
    private ServiceResult<Object> result;

    public ServiceException(String message) {
        super(message);
        this.result = ServiceResultFactory.error(message);
    }

    public ServiceException(String message, Integer code) {
        super(message);
        this.result = ServiceResultFactory.error(message, code);
    }

    public ServiceException(ServiceResult result) {
        super(result.getMessage());
        this.result = result;
    }

    public ServiceException(ServiceResult result, Throwable cause) {
        super(result.getMessage(), cause);
        this.result = result;
    }

    @Override
    public ServiceException get() {
        return this;
    }
}

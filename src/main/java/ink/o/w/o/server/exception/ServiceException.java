package ink.o.w.o.server.exception;

import ink.o.w.o.server.domain.ServiceResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常类, 默认错误码 400
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/4 上午11:12
 */
public class ServiceException extends RuntimeException {

    @Getter
    @Setter
    private ServiceResult<Object> result;

    public ServiceException(ServiceResult result) {
        super(result.getMessage());
        this.result = result;
    }

    public ServiceException(ServiceResult result, Throwable cause) {
        super(result.getMessage(), cause);
        this.result = result;
    }

}

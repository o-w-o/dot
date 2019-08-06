package ink.o.w.o.config.exception;

import ink.o.w.o.config.domain.HttpResponseData;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常类, 默认错误码 400
 * @author symbols@dingtalk.com
 * @version  1.0
 * @date 2019/8/4 上午11:12
 */
public class ServiceException extends RuntimeException {

    @Getter
    @Setter
    private int status = 400;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }

    public ServiceException(String message, int status) {
        super(message);
        this.status = status;
    }

    public HttpResponseData toResponseData() {
        return new HttpResponseData()
            .setMessage(this.getMessage())
            .setStatusCode(this.status);
    }

}

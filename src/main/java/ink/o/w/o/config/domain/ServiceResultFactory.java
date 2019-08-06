package ink.o.w.o.config.domain;

import org.springframework.http.HttpStatus;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午9:23
 */
public class ServiceResultFactory {
    public static ServiceResult success() {
        return new ServiceResult()
            .setSuccess(true);
    }

    public static <T> ServiceResult<T> success(T payload) {
        return new ServiceResult<T>()
            .setSuccess(true)
            .setPayload(payload);
    }

    public static <T> ServiceResult<T> success(T payload, String message) {
        return new ServiceResult<T>()
            .setSuccess(true)
            .setMessage(message)
            .setPayload(payload);
    }

    public static ServiceResult error(String message) {
        return new ServiceResult()
            .setSuccess(false)
            .setMessage(message);
    }

    public static ServiceResult error(String message, int code) {
        return new ServiceResult()
            .setSuccess(false)
            .setMessage(message)
            .setCode(code);
    }
}

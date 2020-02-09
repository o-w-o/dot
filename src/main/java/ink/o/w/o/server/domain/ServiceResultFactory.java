package ink.o.w.o.server.domain;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午9:23
 */
public class ServiceResultFactory {
    public static <T> ServiceResult<T> success() {
        return new ServiceResult<T>()
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

    public static <T> ServiceResult<T> error(String message) {
        return new ServiceResult<T>()
            .setSuccess(false)
            .setMessage(message);
    }

    public static <T> ServiceResult<T> error(String message, int code) {
        return new ServiceResult<T>()
            .setSuccess(false)
            .setMessage(message)
            .setCode(code);
    }
}

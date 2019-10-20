package ink.o.w.o.server.domain;

import org.springframework.http.HttpStatus;

/**
 * 响应结果生成工厂类
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/4 上午10:16
 */
public class HttpResponseDataFactory {

    public static HttpResponseData success() {
        return HttpResponseDataFactory.success(HttpResponseData.DEFAULT_EMPTY_DATA, "OK");
    }

    public static HttpResponseData success(Object data) {
        return HttpResponseDataFactory.success(data, "OK");
    }

    public static HttpResponseData success(Object data, String message) {
        return new HttpResponseData()
            .setStatusCode(HttpStatus.OK)
            .setResult(true)
            .setMessage(message)
            .setData(data);
    }

    public static HttpResponseData error(String message) {
        return HttpResponseDataFactory.error(message, HttpStatus.BAD_REQUEST);
    }

    public static HttpResponseData error(String message, HttpStatus status) {
        return new HttpResponseData()
            .setStatusCode(status)
            .setResult(false)
            .setMessage(message);
    }

    public static HttpResponseData unauthorized() {
        return new HttpResponseData()
            .setStatusCode(HttpStatus.UNAUTHORIZED)
            .setResult(false)
            .setMessage(HttpResponseData.UNAUTHORIZED_DEFAULT_MESSAGE);
    }

    public static HttpResponseData forbidden() {
        return new HttpResponseData()
            .setStatusCode(HttpStatus.FORBIDDEN)
            .setResult(false)
            .setMessage(HttpResponseData.FORBIDDEN_DEFAULT_MESSAGE);
    }


    public static HttpResponseData generateFrom(ServiceResult result, HttpStatus status) {
        return new HttpResponseData()
            .setResult(result.getSuccess())
            .setResultCode(result.getCode())
            .setMessage(result.getMessage())
            .setData(result.getPayload())
            .setStatusCode(status);
    }

    public static HttpResponseData generateFrom(ServiceResult result) {
        return HttpResponseDataFactory.generateFrom(
            result,
            result.getCode() == 0
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST
        );
    }
}

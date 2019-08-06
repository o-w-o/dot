package ink.o.w.o.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * API 结果
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/4 下午6:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponseData {
    public final static Integer OK_RESULT_CODE = 200;
    public final static String UNAUTHORIZED_DEFAULT_MESSAGE = "未授权的访问！";
    public final static String FORBIDDEN_DEFAULT_MESSAGE = "授权不足！";
    public static final Object DEFAULT_EMPTY_DATA = new Object();

    private Integer statusCode;
    private Boolean result;
    private Integer resultCode;
    private String message;
    private String path;
    private Object data;
    private Date timestamp;

    public HttpResponseData setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        this.autocomplete();
        return this;
    }

    public HttpResponseData setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode.value();
        this.autocomplete();
        return this;
    }

    private void autocomplete() {
        if (this.resultCode == null) {
            this.resultCode = this.statusCode;
        }
        if (this.result == null) {
            this.result = this.resultCode < 400;
        }
        if (this.message == null) {
            this.message = HttpStatus.valueOf(this.statusCode).getReasonPhrase();
        }
        if (this.path == null) {
            this.path = "";
        }
        if (this.data == null) {
            this.data = HttpResponseData.DEFAULT_EMPTY_DATA;
        }
        if (this.timestamp == null) {
            this.timestamp = new Date();
        }
    }
}

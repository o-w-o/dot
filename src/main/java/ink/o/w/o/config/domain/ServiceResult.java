package ink.o.w.o.config.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午9:12
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResult<T> {
    private Boolean success;
    private T payload;
    private Integer code;
    private String message;

    public ServiceResult<T> setSuccess(Boolean success) {
        this.success = success;
        this.autocomplete();
        return this;
    }

    private void autocomplete() {
        if (this.code == null) {
            this.code = this.success ? 0 : 400;
        }
        if (this.message == null) {
            this.message = this.success ? "OK" : "NO";
        }
    }
}

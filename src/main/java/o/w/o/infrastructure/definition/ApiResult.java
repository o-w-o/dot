package o.w.o.infrastructure.definition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * APIResult
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/30
 */

@AllArgsConstructor
@Data
public class ApiResult<T> implements Serializable {
  private static final long serialVersionUID = 5569447694538799592L;

  private Boolean success;
  private T payload;
  private Integer code;
  private String message;
  private Date timestamp;

  public ApiResult() {
    this.timestamp = new Date(System.currentTimeMillis());
  }

  /**
   * ServiceResult > APIResult
   *
   * @author symbols@dingtalk.com
   * @date 2020/5/30
   */
  public static ApiResult<Boolean> from(ServiceResult<Boolean> i, String successMessage) {
    return from(i, successMessage, i.getMessage());
  }

  public static ApiResult<Boolean> from(ServiceResult<Boolean> i, String successMessage, String failureMessage) {
    return new ApiResult<Boolean>()
        .setCode(i.getCode())
        .setSuccess(i.isSuccess())
        .setPayload(i.getPayload())
        .setMessage(Objects.nonNull(i.getPayload()) && i.getPayload() ? successMessage : failureMessage);
  }

  public static <T> ApiResult<T> from(ServiceResult<T> i) {
    return new ApiResult<T>()
        .setCode(i.getCode())
        .setSuccess(i.isSuccess())
        .setMessage(i.getMessage())
        .setPayload(i.getPayload());
  }

  /**
   * 通过 payload 构造成功响应
   *
   * @author symbols@dingtalk.com
   * @date 2020/5/30
   */
  public static <T> ApiResult<T> of(T payload) {
    return new ApiResult<T>()
        .setCode(0)
        .setSuccess(true)
        .setMessage("")
        .setPayload(payload);
  }
}

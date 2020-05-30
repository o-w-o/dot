package ink.o.w.o.server.io.api;

import ink.o.w.o.server.io.service.ServiceResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * APIResult
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/30
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class APIResult<T> implements Serializable {
  private static final long serialVersionUID = 5569447694538799592L;

  private Boolean success;
  private T payload;
  private Integer code;
  private String message;
  private Date timestamp = new Date();

  /**
   * ServiceResult > APIResult
   *
   * @author symbols@dingtalk.com
   * @date 2020/5/30
   */
  public static <T> APIResult<T> from(ServiceResult<T> i) {
    return new APIResult<T>()
        .setCode(i.getCode())
        .setSuccess(i.getSuccess())
        .setMessage(i.getMessage())
        .setPayload(i.getPayload());
  }

  /**
   * 通过 payload 构造成功响应
   *
   * @author symbols@dingtalk.com
   * @date 2020/5/30
   */
  public static <T> APIResult<T> of(T payload) {
    return new APIResult<T>()
        .setCode(0)
        .setSuccess(true)
        .setMessage("")
        .setPayload(payload);
  }
}
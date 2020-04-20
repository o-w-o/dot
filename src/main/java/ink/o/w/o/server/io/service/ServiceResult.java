package ink.o.w.o.server.io.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午9:12
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceResult<T> implements Serializable {

  public final static Integer OK_RESULT_CODE = 0;
  public final static Integer NO_RESULT_CODE = 400;
  private static final long serialVersionUID = -1653819158811593636L;
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
      this.code = this.success ? OK_RESULT_CODE : NO_RESULT_CODE;
    }
    if (this.message == null) {
      this.message = this.success ? "OK" : "NO";
    }
  }

  public T guard() {
    if (this.getSuccess()) {
      return this.payload;
    } else {
      throw new ServiceException(this);
    }
  }
}

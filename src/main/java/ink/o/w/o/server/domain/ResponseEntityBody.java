package ink.o.w.o.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

/**
 * API 结果
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/4 下午6:56
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseEntityBody<T> extends RepresentationModel<ResponseEntityBody<T>> implements Serializable {

  private static final long serialVersionUID = 1580635028746898160L;

  private String message = "";
  private T payload;

  static public <T> ResponseEntityBody<T> of(String message, T payload) {
    return new ResponseEntityBody<T>()
        .setPayload(payload)
        .setMessage(message);
  }

  static public ResponseEntityBody<?> of(String message) {
    return of(message, new Object());
  }
}

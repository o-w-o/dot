package ink.o.w.o.server.io.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
/**
 * SystemException
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/30
 */

@Getter
@Setter
@JsonIgnoreProperties({"cause", "stackTrace", "suppressed"})
public class SystemException extends RuntimeException {
  private String scope;
}

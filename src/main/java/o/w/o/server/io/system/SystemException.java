package o.w.o.server.io.system;

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
  private Integer code;
  private String message;

  public SystemException(ExceptionEnum exceptionEnum) {
    this.scope = "system";
    this.code = exceptionEnum.code;
    this.message = exceptionEnum.message;
  }

  public static SystemException of(ExceptionEnum exceptionEnum) {
    return new SystemException(exceptionEnum);
  }

  public static SystemException of(ExceptionEnum exceptionEnum, String message) {
    return new SystemException(exceptionEnum).setMessage(message);
  }

  public enum ExceptionEnum {
    CLONE(1000, "CLONE 异常！"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String message;

    ExceptionEnum(Integer code, String message){
      this.code = code;
      this.message = message;
    }
  }
}

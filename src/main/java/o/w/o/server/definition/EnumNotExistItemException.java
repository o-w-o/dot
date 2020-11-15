package o.w.o.server.definition;

import lombok.Getter;
import lombok.Setter;

/**
 * EnumNotExistItemException
 * - Enum 不存在 Item 异常
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/16
 */
@Getter
@Setter
public class EnumNotExistItemException extends RuntimeException {
  public static final Integer EXCEPTION_CODE = 910000;
  private static final long serialVersionUID = 1182072915622759414L;
  public Class<? extends Enum<?>> enumClazz;
  public String enumName;
  public String itemValue;

  public static EnumNotExistItemException of(Class<? extends Enum<?>> clazz) {
    return new EnumNotExistItemException().setEnumClazz(clazz).setEnumName(clazz.getName()).setItemValue("");
  }

  public static EnumNotExistItemException of(Class<? extends Enum<?>> clazz, String itemValue) {
    return new EnumNotExistItemException().setEnumClazz(clazz).setEnumName(clazz.getName()).setItemValue(itemValue);
  }
}

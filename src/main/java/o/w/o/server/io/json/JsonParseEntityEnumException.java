package o.w.o.server.io.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonParseEntityEnumException extends RuntimeException {
  private static final long serialVersionUID = 1182072915622759414L;

  public static final Integer exceptionCode = 910000;

  public Class<? extends Enum> enumClazz;
  public String enumName;

  public static JsonParseEntityEnumException of(Class<? extends Enum> clazz) {
    return new JsonParseEntityEnumException().setEnumClazz(clazz).setEnumName(clazz.getName());
  }
}

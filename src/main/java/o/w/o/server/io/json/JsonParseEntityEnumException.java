package o.w.o.server.io.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonParseEntityEnumException extends RuntimeException {
  public static Integer exceptionCode = 910000;

  public Class<? extends Enum> enumClazz;
  public String enumName;

  public static JsonParseEntityEnumException of(Class<? extends Enum> clazz) {
    return new JsonParseEntityEnumException().setEnumClazz(clazz).setEnumName(clazz.getName());
  }
}

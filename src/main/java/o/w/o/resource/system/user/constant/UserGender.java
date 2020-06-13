package o.w.o.resource.system.user.constant;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

/**
 * 用户性别枚举
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 17:29
 * @since 1.0.0
 */
public enum UserGender {
  /**
   * 男孩
   *
   * @date 2020/02/13 17:29
   * @since 1.0.0
   */
  BOY(1),
  /**
   * 女孩
   *
   * @date 2020/02/13 17:29
   * @since 1.0.0
   */
  GIRL(-1),
  /**
   * 未知
   *
   * @date 2020/02/13 17:29
   * @since 1.0.0
   */
  UNKNOWN(0);

  @Getter
  Integer value;

  UserGender(Integer value) {
    this.value = value;
  }

  public static class Converter implements AttributeConverter<UserGender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserGender attribute) {
      return attribute.getValue();
    }

    @Override
    public UserGender convertToEntityAttribute(Integer dbData) {
      return Arrays
          .stream(UserGender.values())
          .filter(gender -> gender.getValue().equals(dbData))
          .findFirst()
          .orElse(UNKNOWN);
    }
  }
}

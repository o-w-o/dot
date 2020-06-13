package o.w.o.util.domainconverter.example;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

/**
 * -
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/27
 * @since 1.0.0
 */
public enum DomainEnum {
  /**
   * X
   *
   * @date 2020/02/13 17:29
   * @since 1.0.0
   */
  X(1),
  /**
   * Y
   *
   * @date 2020/02/13 17:29
   * @since 1.0.0
   */
  Y(-1),
  /**
   * 未知
   *
   * @date 2020/02/13 17:29
   * @since 1.0.0
   */
  UNKNOWN(0);

  @Getter
  Integer value;

  DomainEnum(Integer value) {
    this.value = value;
  }

  public static class Converter implements AttributeConverter<DomainEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DomainEnum attribute) {
      return attribute.getValue();
    }

    @Override
    public DomainEnum convertToEntityAttribute(Integer dbData) {
      return Arrays
          .stream(DomainEnum.values())
          .filter(gender -> gender.getValue().equals(dbData))
          .findFirst()
          .orElse(UNKNOWN);
    }
  }
}

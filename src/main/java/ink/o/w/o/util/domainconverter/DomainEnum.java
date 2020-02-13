package ink.o.w.o.util.domainconverter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Convert;
import java.util.Arrays;

/**
 * 用户的性别
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 14:59
 * @since 1.0.0
 */

@EqualsAndHashCode(callSuper = false)
public class DomainEnum extends AbstractDomainEnum<DomainEnum, Integer> {
  /**
   * 男孩
   *
   * @date 2020/02/13 14:59
   * @since 1.0.0
   */
  public static final DomainEnum BOY = new DomainEnum(1);
  /**
   * 女孩
   *
   * @date 2020/02/13 14:59
   * @since 1.0.0
   */
  public static final DomainEnum GIRL = new DomainEnum(-1);
  /**
   * 未知
   *
   * @date 2020/02/13 14:59
   * @since 1.0.0
   */
  public static final DomainEnum UNKNOWN = new DomainEnum(0);

  @Getter
  private Integer value;

  private DomainEnum(Integer value) {
    super(value);
    this.value = value;
  }

  public static DomainEnum of(Integer value) {
    var list = Arrays.asList(BOY, GIRL, UNKNOWN);
    for (DomainEnum gender : list) {
      if (gender.getValue().equals(value)) {
        return gender;
      }
    }

    return UNKNOWN;
  }

  @Slf4j
  @Convert
  public static class UserGenderConverter extends AbstractDomainEnumConverter<DomainEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DomainEnum attribute) {
      logger.info("T -> {}", attribute);
      return attribute.getValue();
    }

    @Override
    public DomainEnum convertToEntityAttribute(Integer dbData) {
      return DomainEnum.of(dbData);
    }
  }
}

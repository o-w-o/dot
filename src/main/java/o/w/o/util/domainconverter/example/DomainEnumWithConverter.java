package o.w.o.util.domainconverter.example;

import o.w.o.util.domainconverter.AbstractDomainEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Convert;
import java.util.Set;

/**
 * DomainEnum
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 14:59
 * @since 1.0.0
 */

@EqualsAndHashCode(callSuper = false)
public class DomainEnumWithConverter extends AbstractDomainEnum<DomainEnumWithConverter, Integer> {
  /**
   * X
   *
   * @date 2020/02/13 14:59
   * @since 1.0.0
   */
  public static final DomainEnumWithConverter X = new DomainEnumWithConverter(1);
  /**
   * Y
   *
   * @date 2020/02/13 14:59
   * @since 1.0.0
   */
  public static final DomainEnumWithConverter Y = new DomainEnumWithConverter(-1);
  /**
   * 未知
   *
   * @date 2020/02/13 14:59
   * @since 1.0.0
   */
  public static final DomainEnumWithConverter UNKNOWN = new DomainEnumWithConverter(0);

  public static final Set<DomainEnumWithConverter> ENUMS = Set.of(X, Y, UNKNOWN);

  @Getter
  private final Integer value;

  private DomainEnumWithConverter(Integer value) {
    super(value);
    this.value = value;
  }

  @Override
  public DomainEnumWithConverter locateValue(Integer integer) {
    return ENUMS.stream()
        .filter(item -> item.getValue().equals(value))
        .findFirst()
        .orElse(UNKNOWN);
  }

  @Slf4j
  @Convert
  public static class DomainEnumConverter implements o.w.o.util.domainconverter.DomainEnumConverter<DomainEnumWithConverter, Integer> {
    @Override
    public DomainEnumWithConverter getDefaultEnum() {
      return UNKNOWN;
    }
  }
}

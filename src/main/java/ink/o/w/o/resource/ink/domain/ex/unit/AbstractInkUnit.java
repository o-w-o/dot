package ink.o.w.o.resource.ink.domain.ex.unit;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ink.o.w.o.resource.ink.constant.InkUnitType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * AbstractInkUnit
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:00
 * @since 1.0.0
 */
@MappedSuperclass
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public abstract class AbstractInkUnit {
  /**
   * id
   *
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "ink-unit-ex-uuid")
  @GenericGenerator(name = "ink-unit-ex-uuid", strategy = "uuid")
  protected Long id;

  @Enumerated(value = EnumType.STRING)
  protected InkUnitType type;
}

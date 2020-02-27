package ink.o.w.o.resource.dot.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ink.o.w.o.resource.dot.constant.DotType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;

/**
 * AbstractInkUnit
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:00
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DotBasic {
  /**
   * id
   *
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "dot-io-uuid")
  @GenericGenerator(name = "dot-io-uuid", strategy = "uuid")
  protected String id;

  @Enumerated(value = EnumType.STRING)
  protected DotType type;

  @Transient
  protected Map<String, Object> source;
}

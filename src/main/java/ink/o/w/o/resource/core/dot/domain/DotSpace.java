package ink.o.w.o.resource.core.dot.domain;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Map;

/**
 * DotSpace
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:00
 * @since 1.0.0
 */

@NoArgsConstructor
@Getter
@Setter

@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")

@MappedSuperclass
public abstract class DotSpace {
  @Transient
  protected Map<String, Object> space;

  /**
   * id
   *
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @GeneratedValue(generator = "dot_space-uuid")
  @GenericGenerator(name = "dot_space-uuid", strategy = "uuid")
  @Id
  protected String id;

  @Transient
  protected DotType.DotTypeEnum type;
}

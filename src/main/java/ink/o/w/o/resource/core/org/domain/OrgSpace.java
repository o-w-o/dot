package ink.o.w.o.resource.core.org.domain;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

/**
 * AbstractInkUnit
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
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")

@MappedSuperclass
public abstract class OrgSpace {
  @Transient
  protected Map<String, Object> space;

  /**
   * id
   *
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @Id
  protected String id;

  @Transient
  protected OrgType type;
}

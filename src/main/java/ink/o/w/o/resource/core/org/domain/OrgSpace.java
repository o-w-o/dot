package ink.o.w.o.resource.core.org.domain;


import ink.o.w.o.server.io.json.annotation.JsonEntityProperty;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
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

@JsonEntityProperty
@JsonTypedSpace

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

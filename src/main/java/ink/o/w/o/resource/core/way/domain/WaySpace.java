package ink.o.w.o.resource.core.way.domain;

import ink.o.w.o.server.io.json.annotation.JsonEntityProperty;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;


/**
 * AbstractInk
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@Getter
@Setter

@JsonEntityProperty
@JsonTypedSpace

@MappedSuperclass
public abstract class WaySpace {

  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "ink_space-uuid")
  @GenericGenerator(name = "ink_space-uuid", strategy = "uuid")
  protected String id;

  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Transient
  protected WayType.WayTypeEnum type;
}

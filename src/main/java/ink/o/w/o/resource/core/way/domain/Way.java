package ink.o.w.o.resource.core.way.domain;

import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * Ink
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@Data

@Entity
@Table(name = "t_way")
public class Way {

  @Transient
  @JsonTypedSpace
  public WaySpace space;
  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "way-uuid")
  @GenericGenerator(name = "way-uuid", strategy = "uuid")
  protected String id;

  /**
   * 标题
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  protected String title;
  protected String description;
  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToOne
  protected WayType type;
  @Lob
  protected String spaceContent;
  @Column(unique = true)
  protected String spaceId;
}

package ink.o.w.o.resource.ink.domain.ex;

import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.domain.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;


/**
 * AbstractInk
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractInk {

  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
  protected Long id;

  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Enumerated(value = EnumType.STRING)
  protected InkType type;

  /**
   * 单位
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @OneToMany
  protected Set<InkUnit> units;

  /**
   * 参考文献
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToMany
  protected Set<InkRef> refs;

  /**
   * 参与者
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @OneToOne
  protected InkParticipants participants;

  /**
   * 特性
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToMany
  protected Set<InkFeature> features;

  /**
   * 标志
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToMany
  protected Set<InkMark> marks;

  /**
   * 标题
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  protected String title;
  protected String description;
}

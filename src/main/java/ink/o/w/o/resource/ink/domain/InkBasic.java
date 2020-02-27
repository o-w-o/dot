package ink.o.w.o.resource.ink.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import ink.o.w.o.resource.dot.domain.Dot;
import ink.o.w.o.resource.ink.constant.InkType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;


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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InkBasic {

  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "ink-uuid")
  @GenericGenerator(name = "ink-uuid", strategy = "uuid")
  protected String id;

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
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  protected Set<Dot> dots;

  /**
   * 参考文献
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToMany(fetch = FetchType.LAZY)
  protected Set<InkRef> refs;

  /**
   * 参与者
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
  protected InkParticipants participants;

  /**
   * 特性
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToMany(fetch = FetchType.LAZY)
  protected Set<InkFeature> features;

  /**
   * 标志
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToMany(fetch = FetchType.LAZY)
  protected Set<InkMark> marks;

  /**
   * 标题
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  protected String title;
  protected String description;

  @Transient
  protected Map<String, Object> source;
}

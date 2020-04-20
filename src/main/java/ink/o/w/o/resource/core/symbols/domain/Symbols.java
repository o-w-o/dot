package ink.o.w.o.resource.core.symbols.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ink.o.w.o.resource.core.org.domain.Org;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;


/**
 * 墨水
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@Data

@Entity
@Table(name = "t_symbols")
public class Symbols {
  @Transient
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
  public SymbolsSpace space;
  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "symbols-uuid")
  @GenericGenerator(name = "symbols-uuid", strategy = "uuid")
  protected String id;
  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToOne
  protected SymbolsType type;
  /**
   * 参与者
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
  protected SymbolsParticipants participants;

  @ManyToMany(fetch = FetchType.LAZY)
  protected Set<Org> orgs;
  
  /**
   * 标题
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  protected String title;
  protected String description;
  @Lob
  protected String spaceContent;
  @Column(unique = true)
  protected String spaceId;
}

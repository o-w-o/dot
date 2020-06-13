package o.w.o.resource.core.symbols.domain;

import o.w.o.resource.core.org.domain.OrgType;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import o.w.o.resource.core.dot.domain.Dot;
import o.w.o.resource.core.dot.domain.DotType;
import o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

@JsonEntityProperty
@JsonTypedSpace

@MappedSuperclass
public abstract class SymbolsSpace {

  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "symbols_space-uuid")
  @GenericGenerator(name = "symbols_space-uuid", strategy = "uuid")
  protected String id;

  @Transient
  protected SymbolsType.SymbolsTypeEnum type;

  @ManyToMany
  @JoinTable(
      name = "t_symbols_accept_dot_types",
      joinColumns = {
          @JoinColumn(name = "dot_type_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "symbols_id")
      }
  )
  protected Set<DotType> acceptDotType;

  @ManyToMany
  protected Set<Dot> dots;

  @ManyToMany
  @JoinTable(
      name = "t_symbols_accept_org_types",
      joinColumns = {
          @JoinColumn(name = "org_type_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "symbols_id")
      }
  )
  protected Set<OrgType> acceptOrgTypes;

  /**
   * 参与者
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
  protected SymbolsParticipants participants;

  @Transient
  protected SymbolsDocument document;


  @Transient
  protected SymbolsRelationships relationship;
}

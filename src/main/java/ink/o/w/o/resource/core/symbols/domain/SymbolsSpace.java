package ink.o.w.o.resource.core.symbols.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.org.domain.OrgType;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")

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

  @Transient
  protected SymbolsSpaceDocument document;
}

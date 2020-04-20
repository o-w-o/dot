package ink.o.w.o.resource.core.symbols.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import ink.o.w.o.resource.core.symbols.constant.SymbolsParticipantsVisibility;
import ink.o.w.o.resource.system.user.domain.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;


/**
 * Ink 的参与者
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 09:12
 * @since 1.0.0
 */
@Entity
@Table(name = "t_symbols_participants")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SymbolsParticipants {
  @Id
  @GeneratedValue
  private Long id;

  @OneToOne(cascade = {CascadeType.REFRESH})
  @JoinColumn(name = "owner_id", referencedColumnName = "id")
  private User owner;

  @OneToMany
  private Set<User> writers;

  @OneToMany
  private Set<User> readers;

  @Enumerated(value = EnumType.STRING)
  private SymbolsParticipantsVisibility visibility;
}

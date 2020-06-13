package o.w.o.resource.core.symbols.domain;

import o.w.o.resource.core.symbols.constant.SymbolsParticipantsVisibility;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import o.w.o.resource.system.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * SymbolsParticipants
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 09:12
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@JsonEntityProperty

@Entity
@Table(name = "t_symbols_participants")
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

  @NotNull
  @Enumerated(value = EnumType.STRING)
  private SymbolsParticipantsVisibility visibility;
}


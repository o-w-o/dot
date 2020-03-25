package ink.o.w.o.resource.ink.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import ink.o.w.o.resource.ink.constant.InkParticipantsVisibility;
import ink.o.w.o.resource.user.domain.User;
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
@Table(name = "t_ink_participants")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InkParticipants {
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
  private InkParticipantsVisibility visibility;
}

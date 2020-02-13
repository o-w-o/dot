package ink.o.w.o.resource.ink.domain;

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
@Data
@Entity
@Table(name = "t_ink_participants")
public class InkParticipants {
  @Id
  Long id;

  @OneToOne
  User owner;

  @OneToMany
  Set<User> writers;

  @OneToMany
  Set<User> readers;

  @Enumerated(value = EnumType.STRING)
  InkParticipantsVisibility visibility;
}

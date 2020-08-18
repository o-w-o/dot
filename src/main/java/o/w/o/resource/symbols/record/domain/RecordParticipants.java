package o.w.o.resource.symbols.record.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.system.user.domain.User;
import o.w.o.server.io.json.annotation.JsonEntityProperty;

import javax.persistence.*;
import java.util.Set;

/**
 * RecordParticipants
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 09:12
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@JsonEntityProperty

@Entity
@Table(name = "t_s_record_participants")
public class RecordParticipants {
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
  private Visibility visibility;

  public enum Visibility {
    /**
     * 无
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    NONE("NONE"),

    /**
     * 无
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    PUBLIC("PUBLIC"),

    /**
     * 黑名单
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    BLACKLIST("BLACKLIST"),

    /**
     * 白名单
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    WHITELIST("WHITELIST");

    /**
     * 类型名称
     *
     * @date 2020/02/12 16:40
     * @since 1.0.0
     */
    @Getter
    private final String visibility;

    Visibility(String visibility) {
      this.visibility = visibility;
    }
  }
}


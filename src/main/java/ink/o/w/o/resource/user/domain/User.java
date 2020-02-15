package ink.o.w.o.resource.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import ink.o.w.o.resource.role.domain.Role;
import ink.o.w.o.resource.user.constant.UserGender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.Set;


/**
 * @author symbols@dingtalk.com
 */
@Entity
@Table(name = "t_user")

@Data
@NoArgsConstructor
public class User extends RepresentationModel<User> implements Serializable {

  private static final long serialVersionUID = 1452277172712371166L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String name;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
  @JoinTable(name = "t_user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  private String nickName;

  @Convert(converter = UserGender.Converter.class)
  private UserGender gender = UserGender.UNKNOWN;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date cTime;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date uTime;

  @PrePersist
  public void recordCreateTime() {
    if (Optional.ofNullable(cTime).isEmpty()) {
      cTime = new Date();
      uTime = new Date();
    }
  }

  @PreUpdate
  public void recordUpdateTime() {
    uTime = new Date();
  }
}

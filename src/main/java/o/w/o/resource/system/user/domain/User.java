package o.w.o.resource.system.user.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import o.w.o.resource.system.role.domain.Role;
import o.w.o.resource.system.user.constant.UserGender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * @author symbols@dingtalk.com
 */
@NoArgsConstructor
@Data

@JsonInclude(JsonInclude.Include.NON_NULL)

@Entity
@Table(name = "t_sys_user")
public class User implements Serializable {

  private static final long serialVersionUID = 1452277172712371166L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String name;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
  @JoinTable(name = "t_sys_user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

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

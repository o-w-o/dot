package ink.o.w.o.resource.system.authorization.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ink.o.w.o.resource.system.role.constant.Roles;
import ink.o.w.o.resource.system.role.util.RoleHelper;
import ink.o.w.o.resource.system.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * @author LongY
 */
@NoArgsConstructor
@Setter
@Getter

@JsonIgnoreProperties(ignoreUnknown = true)

@Component
public class AuthorizedUser extends User implements UserDetails {

  private Integer id;

  private String username;
  private String password;
  private String ip;
  private Collection<? extends GrantedAuthority> authorities;

  public static AuthorizedUser parse(User user) {
    return new AuthorizedUser()
        .setId(user.getId())
        .setUsername(user.getName())
        .setPassword(user.getPassword())
        .setAuthorities(RoleHelper.toAuthorities(user.getRoles()));
  }

  public static AuthorizedUser anonymousUser(String ip) {
      return new AuthorizedUser()
          .setUsername("anno")
          .setIp(ip)
          .setAuthorities(RoleHelper.toAuthorities(Set.of(Roles.ANONYMOUS)));
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 密码是否未过期
   *
   * @return boolean 密码未过期
   */
  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 账户是否激活
   *
   * @return 返回 true 账户已激活
   */
  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }
}

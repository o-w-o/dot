package o.w.o.domain.core.authorization.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import o.w.o.domain.core.authentication.domain.AuthenticationPayload;
import o.w.o.domain.core.role.util.RoleUtil;
import o.w.o.infrastructure.definition.ServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * AuthorizedUser
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/27
 */
@NoArgsConstructor
@Setter
@Getter
public class AuthorizedUser implements UserDetails {
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  private boolean accountNonExpired = true;
  private boolean accountNonLocked = true;
  private boolean credentialsNonExpired = true;
  private boolean enabled = true;

  private String ip;
  private String stubId;
  private Integer id;
  private AuthenticationPayload authenticationPayload;

  private boolean anonymous = false;

  public static AuthorizedUser from(AuthorizationJwt jwt, AuthenticationPayload payload) {
    if (Objects.isNull(jwt.getUip())) {
      throw ServiceException.of("IP 字段缺失");
    }

    return new AuthorizedUser()
        .setId(jwt.getUid())
        .setIp(jwt.getUip())
        .setAnonymous(false)
        .setUsername(jwt.getAud())
        .setPassword("")
        .setAuthenticationPayload(payload)
        .setStubId(AuthorizationStub.generateId(jwt))
        .setAuthorities(
            RoleUtil.toAuthorities(RoleUtil.from(jwt.getRol()))
        );
  }

  public static AuthorizedUser createAnonymousUser(String ip) {
    return new AuthorizedUser()
        .setIp(ip)
        .setAnonymous(true);
  }

  /**
   * 账户是否过期
   */
  @Override
  public boolean isAccountNonExpired() {
    return this.accountNonExpired;
  }

  /**
   * 账户是否锁定
   */
  @Override
  public boolean isAccountNonLocked() {
    return this.accountNonLocked;
  }

  /**
   * 密码是否未过期
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return this.credentialsNonExpired;
  }

  /**
   * 账户是否激活
   */
  @Override
  public boolean isEnabled() {
    return this.enabled;
  }
}

package ink.o.w.o.server.domain;

import ink.o.w.o.resource.user.util.UserHelper;
import ink.o.w.o.resource.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author LongY
 */
@JsonIgnoreProperties(ignoreUnknown = true)

@Component
@Setter
@Getter
@NoArgsConstructor
public class AuthorizedUser extends User implements UserDetails {

    private Integer id;

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

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
     * @return ture 密码未过期
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

    public static AuthorizedUser parse(User user) {
        return new AuthorizedUser()
            .setId(user.getId())
            .setUsername(user.getName())
            .setPassword(user.getPassword())
            .setAuthorities(mapToGrantedAuthorities(UserHelper.getRoles(user)));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        List<GrantedAuthority> list = new ArrayList<>();

        authorities.forEach((authority) -> {
            list.add(new SimpleGrantedAuthority("ROLE_" + authority));
        });

        return list;
    }
}

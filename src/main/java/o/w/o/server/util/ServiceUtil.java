package o.w.o.server.util;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 应用 Service 上下文辅助类
 * - SecurityContext
 * - RequestContext
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */
@Slf4j
public class ServiceUtil {

  public static void authorize(AuthorizedUser authorizedUser, HttpServletRequest request) {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(
            authorizedUser,
            new WebAuthenticationDetailsSource().buildDetails(request),
            authorizedUser.getAuthorities()
        )
    );
  }

  public static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static Optional<Authentication> fetchAuthentication() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
  }

  public static AuthorizedUser getPrincipal() {
    return (AuthorizedUser) getAuthentication().getPrincipal();
  }

  public static Integer getPrincipalUserId() {
    return getPrincipal().getId();
  }

  public static Optional<Integer> fetchPrincipalUserId() {
    return Optional.ofNullable(getPrincipal().getId());
  }

  public static String getPrincipalIp() {
    return getPrincipal().getIp();
  }
}

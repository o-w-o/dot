package o.w.o.server.io.service;

import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.RequestContextHolder;

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
public class ServiceContext {
  public static final String IP_ATTRIBUTE_KEY = "ip";
  public static final Integer IP_ATTRIBUTE_SCOPE = 0;

  public static void attachAuthenticationToSecurityContext(AuthorizedUser authorizedUser, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        authorizedUser,
        new WebAuthenticationDetailsSource().buildDetails(request),
        authorizedUser.getAuthorities()
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public static Authentication getAuthenticationFormSecurityContext() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static AuthorizedUser getAuthorizedUserFormSecurityContext() {
    return (AuthorizedUser) getAuthenticationFormSecurityContext().getPrincipal();
  }

  public static Integer getUserIdFormSecurityContext() {
    return getAuthorizedUserFormSecurityContext().getId();
  }

  public static Optional<AuthorizedUser> fetchAuthorizedUserFormSecurityContext() {
    return Optional
        .ofNullable(
            SecurityContextHolder
                .getContext()
                .getAuthentication()
        )
        .map(Authentication::getPrincipal)
        .map((u) -> (AuthorizedUser) u);
  }

  public static Optional<Integer> fetchUserIdFormSecurityContext() {
    return fetchAuthorizedUserFormSecurityContext().map(AuthorizedUser::getId);
  }

  public static Optional<AuthorizedUser> fetchAnonymousUserFormSecurityContext() {
    return fetchIpFromRequestContext().map(AuthorizedUser::anonymousUser);
  }

  public static void setIpToRequestContext(@NonNull String ip) {
    RequestContextHolder.currentRequestAttributes().setAttribute(IP_ATTRIBUTE_KEY, ip, IP_ATTRIBUTE_SCOPE);
    logger.info(
        "currentRequestAttributes {}", RequestContextHolder.currentRequestAttributes().getAttribute(IP_ATTRIBUTE_KEY, IP_ATTRIBUTE_SCOPE)
    );
  }

  public static Optional<String> fetchIpFromRequestContext() {
    return Optional.ofNullable(
        String.valueOf(RequestContextHolder.currentRequestAttributes().getAttribute("ip", 0))
    );
  }
}

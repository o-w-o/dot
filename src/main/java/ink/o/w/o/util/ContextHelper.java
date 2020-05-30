package ink.o.w.o.util;

import ink.o.w.o.resource.system.authorization.domain.AuthorizedUser;
import ink.o.w.o.server.io.api.annotation.APIResourceSchema;
import ink.o.w.o.server.io.api.APISchemata;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 应用上下文辅助类
 * - ApplicationContext
 * - SecurityContext
 * - RequestContext
 * - APIContext
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */
@Slf4j
public class ContextHelper {
  private static ApplicationContext applicationContext;
  public static final String IP_ATTRIBUTE_KEY = "ip";
  public static final Integer IP_ATTRIBUTE_SCOPE = 0;
  private static final Map<Class<?>, APISchemata> apiContext = new ConcurrentHashMap<>();

  public static void setApplicationContext(ApplicationContext ctx) {
    logger.info("setApplicationContext -> {}", ctx.getClass().getName());
    applicationContext = ctx;
  }

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

  public static void attachSchemaToAPIContext(Class<?> clazz, APISchemata schema) {
    apiContext.put(clazz, schema);
  }

  public static Map<String, String> fetchAPIContext() {
    var map = new HashMap<String, String>();
    apiContext.forEach((aClass, apiSchemata) -> {
      if ("/".equals(apiSchemata.getNamespace())) {
        map.put(apiSchemata.getNamespace(), String.format("/%s", APIResourceSchema.SCHEMA));
      } else {
        map.put(apiSchemata.getNamespace(), String.format("/%s/%s", apiSchemata.getNamespace(), APIResourceSchema.SCHEMA));
      }
    });
    return map;
  }

  public static Optional<APISchemata> fetchAPIContext(Class<?> clazz) {
    return Optional.ofNullable(apiContext.get(clazz));
  }

  public static <T extends AbstractContext> void attachApplicationContextToInstance(T instance) {
    instance.setApplicationContext(applicationContext);
  }

  @Data
  @NoArgsConstructor
  public static abstract class AbstractContext {
    protected ApplicationContext applicationContext;
    protected Boolean resourceInitialStatus = false;
    protected Boolean contextInitialStatus = false;

    protected Boolean isContextInitCompletely() {
      return this.applicationContext != null;
    }

    public <T> T getBean(Class<T> beanClazz) {
      return this.applicationContext.getBean(beanClazz);
    }
  }
}

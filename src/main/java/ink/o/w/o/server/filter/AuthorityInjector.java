package ink.o.w.o.server.filter;

import ink.o.w.o.resource.system.authorization.domain.AuthorizationPayload;
import ink.o.w.o.resource.system.authorization.domain.AuthorizedUser;
import ink.o.w.o.resource.system.authorization.service.AuthorizedJwtStoreService;
import ink.o.w.o.resource.system.role.util.RoleHelper;
import ink.o.w.o.resource.system.user.domain.User;
import ink.o.w.o.server.io.api.ResponseEntityExceptionBody;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.util.HttpHelper;
import ink.o.w.o.util.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.commons.codec.CharEncoding.UTF_8;

/**
 * 每次请求前的权限注入过滤器
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/2 下午11:17
 */
@Slf4j
@Filter(name = "AuthorityInjector")
public class AuthorityInjector extends OncePerRequestFilter {

  @Autowired
  private JsonHelper jsonHelper;

  @Autowired
  private AuthorizedJwtStoreService authorizedJwtStoreService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String ip = getIpAddress(request);
    logger.info("Referer: [{}], UA: [{}], IP: [{}]",
        request.getHeader("Referer"),
        request.getHeader("User-Agent"),
        ip
    );

    ServiceResult<AuthorizationPayload> authorizationPayloadServiceResult = authorizedJwtStoreService.validate(request);
    AuthorizationPayload authorizationPayload = authorizationPayloadServiceResult.getPayload();

    if (authorizationPayloadServiceResult.getSuccess()) {
      String userName = authorizationPayload.getJwt().getUid().toString();
      String userRoles = authorizationPayload.getJwt().getRol();
      Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

      if (userAuthentication == null || !userAuthentication.isAuthenticated()) {
        logger.info("用户未授权,尝试注入权限[rol -> {}]……", userRoles);
        AuthorizedUser authorizedUser = AuthorizedUser
            .parse(
                new User()
                    .setName(userName)
                    .setRoles(RoleHelper.fromRolesString(userRoles))
            )
            .setId(authorizationPayload.getJwt().getUid())
            .setIp(ip);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            authorizedUser,
            new WebAuthenticationDetailsSource().buildDetails(request),
            authorizedUser.getAuthorities()
        );

        logger.info("为可授权限用户: " + userName + "，此次访问注入权限：" + jsonHelper.toJsonString(authorizedUser.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        logger.info("用户已授权！");
      }
    } else {
      var anonymousUser = AuthorizedUser.anonymousUser(ip);
      SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(ip, anonymousUser, anonymousUser.getAuthorities()));
    }

    if (authorizationPayload.isJwtHeaderValid() && !authorizationPayload.isJwtParsePassed()) {
      this.handlerJwtAuthenticationException(request, response);
    } else {
      chain.doFilter(request, response);
    }
  }

  private void handlerJwtAuthenticationException(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8);
    response.setStatus(HttpStatus.FORBIDDEN.value());

    try (PrintWriter writer = response.getWriter()) {
      writer.write(jsonHelper.toJsonString(
          ResponseEntityExceptionBody.error(HttpHelper.formatResponseDataMessage(request).apply("用户授权信息解析异常，授权终止！"))
              .setPath(request.getRequestURI())
              .setCode(12333)
      ));
      writer.flush();
    }
  }

  private Boolean getIpAddressNextProxy(String ip) {
    return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
  }

  private String getIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    logger.info("use x-forwarded-for -> [{}]", ip);

    if (getIpAddressNextProxy(ip)) {
      ip = request.getRemoteAddr();
      logger.info("use RemoteAddr");
    }

    // 如果是多级代理，那么取第一个 ip 为客户端 ip
    if (ip != null && ip.contains(",")) {
      ip = ip.substring(0, ip.indexOf(",")).trim();
    }

    return ip;
  }

}

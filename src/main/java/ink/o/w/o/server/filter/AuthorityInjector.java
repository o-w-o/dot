package ink.o.w.o.server.filter;

import ink.o.w.o.resource.role.constant.Roles;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.server.domain.*;
import ink.o.w.o.server.service.AuthorizedJwtStoreService;
import ink.o.w.o.util.HttpHelper;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    JSONHelper jsonHelper;

    @Autowired
    AuthorizedJwtStoreService authorizedJwtStoreService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        logger.info("Referer: [" + request.getHeader("Referer") + "]");
        logger.info("UA: [" + request.getHeader("User-Agent") + "] ; IP: " + getIpAddress(request));

        ServiceResult<AuthorizationPayload> authorizationPayloadServiceResult = authorizedJwtStoreService.validate(request);
        AuthorizationPayload authorizationPayload = authorizationPayloadServiceResult.getPayload();

        if (authorizationPayloadServiceResult.getSuccess()) {
            String userName = authorizationPayload.getJwt().getUid().toString();
            String userRoles = authorizationPayload.getJwt().getRol();
            Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

            if (userAuthentication == null || !userAuthentication.isAuthenticated()) {
                logger.info("用户未授权,尝试注入权限[rol -> {}]……", userRoles);
                AuthorizedUser authorizedUser = AuthorizedUser.parse(
                    new User()
                        .setName(userName)
                        .setRoles(Roles.fromRolesString(userRoles))
                );

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authorizedUser, null, authorizedUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                logger.info("为可授权限用户: " + userName + "，此次访问注入权限：" + jsonHelper.toJSONString(authorizedUser.getAuthorities()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("用户已授权！");
            }
        }

        if (authorizationPayload.isJwtHeaderValid() && !authorizationPayload.isJwtParsePassed()) {
            this.handlerJwtAuthenticationException(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void handlerJwtAuthenticationException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonHelper.toJSONString(
                HttpResponseDataFactory.error(HttpHelper.formatResponseDataMessage(request).apply("用户授权信息解析异常，授权终止！"))
                    .setPath(request.getRequestURI())
                    .setResultCode(12333)
            ));
            writer.flush();
        }
    }

    private Boolean getIpAddressNextProxy(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (getIpAddressNextProxy(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (getIpAddressNextProxy(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (getIpAddressNextProxy(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (getIpAddressNextProxy(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (getIpAddressNextProxy(ip)) {
            ip = request.getRemoteAddr();
        }

        // 如果是多级代理，那么取第一个 ip 为客户端 ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }

}

package ink.o.w.o.server.auth.filter;

import ink.o.w.o.config.domain.HttpResponseDataFactory;
import ink.o.w.o.server.auth.domain.AuthorizedJwt;
import ink.o.w.o.server.auth.domain.AuthorizedUser;
import ink.o.w.o.util.JSONUtil;
import ink.o.w.o.util.RequestUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
@Filter(name = "AuthorityInjector")
public class AuthorityInjector extends OncePerRequestFilter {

    @Autowired
    @Qualifier("authorizedUserServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        logger.info("UA: [" + request.getHeader("User-Agent") + "] ; IP: " + getIpAddress(request));
        boolean hasJwtAuthenticationException = false;

        // HTTP头部 是否携带 Token
        if (isHttpHeaderHasToken(request)) {

            String username;
            String authorization = request.getHeader(AuthorizedJwt.REQUEST_AUTHORIZATION_KEY);
            String jwt = authorization.substring(AuthorizedJwt.AUTHORIZATION_PREFIX.length());
            Claims jwtClaims;
            try {
                jwtClaims = AuthorizedJwt.getClaimsFromJwt(jwt);
                username = jwtClaims.getAudience();
            } catch (JwtException e) {
                jwtClaims = null;
                username = null;
            }

            logger.info("用户: " + username + " Token: " + jwt);
            logger.info("用户授权检验……");
            if (username == null || jwtClaims == null) {
                hasJwtAuthenticationException = true;
                logger.info("用户授权信息不足，授权终止！");
            } else if (SecurityContextHolder.getContext().getAuthentication() == null) {

                logger.info("用户未授权,尝试注入权限：");
                logger.info("用户TOKEN检验……");
                // 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
                // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
                // 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
                // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
                // TODO 也可以使用 redis 代替数据库查询
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (AuthorizedJwt.valid(jwtClaims, (AuthorizedUser) userDetails)) {
                    logger.info("用户TOKEN检验通过！");

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    logger.info("为可授权限用户: " + username + "，此次访问注入权限：" + JSONUtil.toJSONString(userDetails.getAuthorities()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.info("用户 TOKEN 检验未通过！");
                }
            } else {
                logger.info("用户已授权！");
            }
        }

        if (!hasJwtAuthenticationException) {
            chain.doFilter(request, response);
        } else {
            this.handlerJwtAuthenticationException(request, response);
        }
    }

    private void handlerJwtAuthenticationException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSONUtil.toJSONString(
                HttpResponseDataFactory.error(RequestUtil.formatResponseDataMessage(request).apply("用户授权信息解析异常，授权终止！"))
                    .setPath(request.getRequestURI())
                    .setResultCode(12333)
            ));
            writer.flush();
        }

    }

    private boolean isHttpHeaderHasToken(HttpServletRequest request) {

        String httpHeader = request.getHeader(AuthorizedJwt.REQUEST_AUTHORIZATION_KEY);

        if (httpHeader == null) {
            logger.info("HTTP 头部 是否携带 Token ? " + false);
            return false;
        } else {
            logger.info("HTTP 头部 是否携带 Token ? " + true);
            logger.info("HTTP 头部 -" + AuthorizedJwt.REQUEST_AUTHORIZATION_KEY + "- 字段值是否以 [" + AuthorizedJwt.AUTHORIZATION_PREFIX + "] 开始 ? " + httpHeader.startsWith(AuthorizedJwt.AUTHORIZATION_PREFIX));
            return httpHeader.startsWith(AuthorizedJwt.AUTHORIZATION_PREFIX);
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

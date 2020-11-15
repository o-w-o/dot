package o.w.o.resource.system.authentication.configuration;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authentication.domain.AuthenticationPayload;
import o.w.o.resource.system.authentication.service.AuthenticationService;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import o.w.o.server.definition.ApiException;
import o.w.o.server.helper.HttpHelper;
import o.w.o.server.util.ServiceUtil;
import o.w.o.util.RequestUtil;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 每次请求前的权限注入过滤器
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/2 下午11:17
 */
@Slf4j
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter implements OrderedFilter {
  public static final int ORDER = 1600;

  @Resource
  private HttpHelper httpHelper;

  @Resource
  private AuthenticationService authenticationService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain) throws ServletException, IOException {

    String ip = RequestUtil.getIpAddress(request);

    var reportOfRequest = this.authenticationService.matchRequestHeader(request).guard().setRequestIp(ip);

    if (reportOfRequest.isReqHeaderValid()) {
      var report = this.authenticationService.validateJwt(reportOfRequest.getJwt()).guard();


      if (report.isJwtValid()) {
        var jwt = report.getJwt();

        var ipMatched = authenticationService.matchIp(jwt, ip);

        if (!ipMatched.isSuccess()) {
          httpHelper.handlerRequestException(request, response, exception(ipMatched.getMessage()));
          return;
        }

        var stubMatched = authenticationService.matchStub(jwt);
        if (!stubMatched.isSuccess()) {
          httpHelper.handlerRequestException(request, response, exception(stubMatched.getMessage()));
          return;
        }

        if (ServiceUtil.fetchAuthentication().isEmpty()) {
          logger.info("用户未授权,尝试注入权限[rol -> {}]……", jwt.getRol());
          var payload = new AuthenticationPayload()
              .setAccessToken(report.getJwts());

          var authorizedUser = AuthorizedUser.from(jwt, payload);

          ServiceUtil.authorize(authorizedUser, request);
          logger.info("为可授权限用户: [{}]，此次访问注入权限： [{}]", jwt.getAud(), authorizedUser.getAuthorities());
        } else {
          logger.info("用户已授权！");
        }
      } else {
        httpHelper.handlerRequestException(request, response, exception(report.getMessage()));
        return;
      }
    } else if (reportOfRequest.isReqHeaderNonEmpty()) {
      httpHelper.handlerRequestException(request, response, exception(reportOfRequest.getMessage()));
      return;
    } else {
      // 注入匿名用户
      ServiceUtil.authorize(AuthorizedUser.createAnonymousUser(ip), request);
    }

    chain.doFilter(request, response);
  }

  public ApiException exception(String message) {
    return ApiException.of(String.format("用户授权信息解析异常 [ %s ]，授权终止！", message), HttpStatus.UNAUTHORIZED);
  }

  @Override
  public int getOrder() {
    return 1600;
  }
}

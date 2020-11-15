package o.w.o.resource.system.authentication.service;

import o.w.o.resource.system.authentication.domain.AuthenticationReportOfJwt;
import o.w.o.resource.system.authentication.domain.AuthenticationReportOfRequest;
import o.w.o.resource.system.authorization.domain.AuthorizationJwt;
import o.w.o.server.definition.ServiceResult;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
  /**
   * 验证 request header 信息是否合法
   *
   * @param request http request
   * @return {@link AuthenticationReportOfRequest}
   * @author symbols@dingtalk.com
   * @date 2019/10/19 20:13
   * @since 1.0
   */
  ServiceResult<AuthenticationReportOfRequest> matchRequestHeader(HttpServletRequest request);

  /**
   * 验证 jwt 是否合法
   *
   * @param jwt accessToken
   * @return {@link AuthenticationReportOfJwt}
   * @author symbols@dingtalk.com
   * @date 2019/10/19 20:13
   * @since 1.0
   */
  ServiceResult<AuthenticationReportOfJwt> validateJwt(String jwt);

  /**
   * 验证 ip（请求上下文中获取） 是否 匹配
   *
   * @param authorizationJwt jwt
   * @return {@link AuthenticationReportOfJwt}
   * @author symbols@dingtalk.com
   * @date 2019/10/19 20:13
   * @since 1.0
   */
  ServiceResult<Boolean> matchRequestIp(AuthorizationJwt authorizationJwt);

  /**
   * 验证 ip 是否 匹配指定 jwt
   *
   * @param authorizationJwt jwt
   * @param ip               ip
   * @return {@link AuthenticationReportOfJwt}
   * @author symbols@dingtalk.com
   * @date 2019/10/19 20:13
   * @since 1.0
   */
  ServiceResult<Boolean> matchIp(AuthorizationJwt authorizationJwt, String ip);


  /**
   * 验证 存根 是否 匹配
   *
   * @param authorizationJwt jwt
   * @return {@link AuthenticationReportOfJwt}
   * @author symbols@dingtalk.com
   * @date 2019/10/19 20:13
   * @since 1.0
   */
  ServiceResult<Boolean> matchStub(AuthorizationJwt authorizationJwt);
}

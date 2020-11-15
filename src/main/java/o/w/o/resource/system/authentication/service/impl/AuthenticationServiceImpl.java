package o.w.o.resource.system.authentication.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authentication.domain.AuthenticationReportOfJwt;
import o.w.o.resource.system.authentication.domain.AuthenticationReportOfRequest;
import o.w.o.resource.system.authentication.service.AuthenticationService;
import o.w.o.resource.system.authorization.domain.AuthorizationJwt;
import o.w.o.resource.system.authorization.domain.AuthorizationStub;
import o.w.o.resource.system.authorization.domain.property.AuthorizationHeader;
import o.w.o.resource.system.authorization.repository.AuthorizationStubRepository;
import o.w.o.server.definition.ServiceResult;
import o.w.o.server.util.ServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 认证服务
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/13
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  @Resource
  private AuthorizationStubRepository authorizationStubRepository;

  @Override
  public ServiceResult<AuthenticationReportOfJwt> validateJwt(String jwt) {
    var report = new AuthenticationReportOfJwt();
    try {
      AuthorizationJwt authorizationJwt = AuthorizationJwt.copy(jwt);
      report.setJwt(authorizationJwt);
      report.setJwtNonExpired(true);
      report.setJwtParsed(true);
    } catch (JwtException e) {

      logger.error("AuthenticationReport: JWT parse exception -> [{}]", e.getMessage());
      if (e instanceof ExpiredJwtException) {
        report.setJwtNonExpired(false);
        report.setMessage("用户 Token 时效失效");
        return ServiceResult.success(report);
      }

      report.setJwtParsed(false);
      report.setMessage("用户 Token 解析失败");
      return ServiceResult.success(report);
    }

    report.setJwtValid(true);
    return ServiceResult.success(report);
  }

  @Override
  public ServiceResult<AuthenticationReportOfRequest> matchRequestHeader(HttpServletRequest request) {
    var report = new AuthenticationReportOfRequest()
        .setReq(request);

    String httpHeader = request.getHeader(AuthorizationHeader.HEADER_KEY);

    if (Objects.isNull(httpHeader)) {
      report.setMessage("HTTP 头部 未携带 认证信息");
      return ServiceResult.success(report);
    } else {
      report.setReqHeaderExist(true);
    }

    if (httpHeader.startsWith(AuthorizationHeader.HEADER_VAL_PREFIX)) {
      report.setReqHeaderPatterned(true);
    } else {
      report.setMessage("HTTP 头部 认证格式异常");
      return ServiceResult.success(report);
    }

    var accessToken = httpHeader
        .substring(AuthorizationHeader.HEADER_VAL_PREFIX.length());

    if (StringUtils.isNotBlank(accessToken)) {
      report.setReqHeaderNonEmpty(true);
    } else {
      report.setMessage("HTTP 头部 未携带 Token");
      return ServiceResult.success(report);
    }

    report.setJwt(accessToken);
    report.setReqHeaderValid(true);
    return ServiceResult.success(report);
  }

  @Override
  public ServiceResult<Boolean> matchRequestIp(AuthorizationJwt jwt) {
    return ServiceResult.of(
        Objects.nonNull(jwt.getUip()) && jwt.getUip().equals(ServiceUtil.getPrincipalIp()),
        "用户 Token IP 异常"
    );
  }

  @Override
  public ServiceResult<Boolean> matchIp(AuthorizationJwt jwt, String ip) {
    return ServiceResult.of(
        Objects.nonNull(jwt.getUip()) && ip.equals(jwt.getUip()),
        "用户 Token IP 异常"
    );
  }

  @Override
  public ServiceResult<Boolean> matchStub(AuthorizationJwt jwt) {
    return ServiceResult.of(
        authorizationStubRepository.existsById(AuthorizationStub.generateId(jwt)),
        "用户 Token 存根异常，令牌可能已被注销"
    );
  }
}

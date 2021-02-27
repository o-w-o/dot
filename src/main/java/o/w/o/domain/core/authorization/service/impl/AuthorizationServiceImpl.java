package o.w.o.domain.core.authorization.service.impl;

import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.authentication.service.AuthenticationService;
import o.w.o.domain.core.authorization.domain.AuthorizedJwt;
import o.w.o.domain.core.authorization.service.AuthorizationService;
import o.w.o.domain.core.authorization.service.AuthorizationStubService;
import o.w.o.domain.core.user.domain.User;
import o.w.o.domain.core.user.repository.UserRepository;
import o.w.o.infrastructure.definition.ServiceException;
import o.w.o.infrastructure.definition.ServiceResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 权限校验服务
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/3 下午2:13
 */
@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
  @Resource
  private PasswordEncoder passwordEncoder;

  @Resource
  private UserRepository userRepository;

  @Resource
  private AuthenticationService authenticationService;

  @Resource
  private AuthorizationStubService authorizationStubService;

  @Override
  public ServiceResult<AuthorizedJwt> authorize(String username, String password) {
    User user = userRepository
        .findUserByName(username).orElseThrow(new ServiceException(String.format("用户 [ %s ] 不存在！", username)));

    if (passwordEncoder.matches(password, user.getPassword())) {
      return ServiceResult.success(
          authorizationStubService.register(user).guard()
      );
    }

    return ServiceResult.error("登录密码错误！");
  }

  @Override
  public ServiceResult<AuthorizedJwt> authorize(String refreshToken) {
    if (refreshToken == null || "".equals(refreshToken)) {
      return ServiceResult.error("用户 refreshToken 为空！");
    }

    var report = authenticationService.validateJwt(refreshToken).guard();

    logger.info("report -> {}", report);
    if (report.isJwtValid()) {
      return ServiceResult.success(
          authorizationStubService
              .refresh(report.getJwt())
              .guard()
      );
    }

    if (!report.isJwtNonExpired()) {
      return ServiceResult.error("用户 refreshToken 已过期！");
    }

    return ServiceResult.error(report.getMessage());
  }

  @Override
  public ServiceResult<Boolean> revoke(String stubId) {
    return authorizationStubService.revoke(stubId);
  }
}

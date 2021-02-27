package o.w.o.domain.core.authorization.service.impl;

import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.authorization.domain.AuthorizationJwt;
import o.w.o.domain.core.authorization.domain.AuthorizationStub;
import o.w.o.domain.core.authorization.domain.AuthorizedJwt;
import o.w.o.domain.core.authorization.repository.AuthorizationStubRepository;
import o.w.o.domain.core.authorization.service.AuthorizationStubService;
import o.w.o.domain.core.role.util.RoleUtil;
import o.w.o.domain.core.user.domain.User;
import o.w.o.infrastructure.definition.ServiceException;
import o.w.o.infrastructure.definition.ServiceResult;
import o.w.o.infrastructure.util.ServiceUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class AuthorizationStubServiceImpl implements AuthorizationStubService {
  @Resource
  private AuthorizationStubRepository authorizationStubRepository;

  @Override
  public ServiceResult<AuthorizedJwt> register(User user) {
    var now = new Date();
    var ip = ServiceUtil.getPrincipalIp();

    var authorizationJwt = AuthorizationJwt.from(user)
        .setUip(ip);

    var authorizedJwts = new AuthorizedJwt()
        .setAccessToken(authorizationJwt.setExp(DateUtils.addMinutes(now, 15)).toString())
        .setRefreshToken(authorizationJwt.setExp(DateUtils.addDays(now, 15)).toString());

    var authorizationStub = new AuthorizationStub()
        .setIp(ip)
        .setId(AuthorizationStub.generateId(authorizationJwt))
        .setUid(user.getId())
        .setJti(authorizationJwt.getJti());

    authorizationStubRepository.save(
        authorizationStub
    );

    return ServiceResult.success(authorizedJwts);
  }

  @Override
  public ServiceResult<AuthorizedJwt> refresh(AuthorizationJwt jwt) {
    var stubId = AuthorizationStub.generateId(jwt);
    var stub = authorizationStubRepository
        .findById(stubId)
        .orElseThrow(new ServiceException("用户 refreshToken 无效，未查到有效 accessToken！" + stubId));

    authorizationStubRepository.deleteById(stubId);

    return register(
        new User()
            .setId(stub.getUid())
            .setName(jwt.getAud())
            .setRoles(RoleUtil.from(jwt.getRol()))
    );
  }

  @Override
  public ServiceResult<Boolean> revoke(String id) {
    logger.info("revoke -> id [{}]", id);

    if (authorizationStubRepository.existsById(id)) {
      authorizationStubRepository.deleteById(id);
      return ServiceResult.success(true);
    }

    return ServiceResult.error("令牌已被注销！");
  }

  @Override
  public ServiceResult<Boolean> revokeAll(Integer userId) {
    authorizationStubRepository.deleteAll(authorizationStubRepository.findByUid(userId));
    return ServiceResult.success(true);
  }

  @Override
  public ServiceResult<Boolean> reset() {
    authorizationStubRepository.deleteAll();
    return ServiceResult.success(true);
  }
}

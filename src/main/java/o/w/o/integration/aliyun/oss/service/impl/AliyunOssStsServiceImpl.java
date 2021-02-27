package o.w.o.integration.aliyun.oss.service.impl;

import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.infrastructure.definition.ServiceException;
import o.w.o.infrastructure.definition.ServiceResult;
import o.w.o.infrastructure.util.ServiceUtil;
import o.w.o.integration.aliyun.oss.domain.sts.OssRamPolicyGenerator;
import o.w.o.integration.aliyun.oss.service.AliyunOssStsService;
import o.w.o.integration.aliyun.sts.domain.Policy;
import o.w.o.integration.aliyun.sts.domain.Sts;
import o.w.o.integration.aliyun.sts.service.AliyunStsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AliyunOssStsServiceImpl implements AliyunOssStsService {
  @Resource
  private AliyunStsService aliyunStsService;
  @Resource
  private OssRamPolicyGenerator ossRamPolicyGenerator;

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForUser(OssRamPolicyGenerator.Preset preset, AuthorizedUser user) {
    var optionalSts = aliyunStsService.createSts(
        user,ossRamPolicyGenerator.of(preset, user)
    );

    if (optionalSts.isSuccess()) {
      return ServiceResult.success(
          Sts.Credentials.of(optionalSts.guard().getCredentials())
      );
    }

    return ServiceResult.error("授权失败！");
  }


  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForUser(OssRamPolicyGenerator.Preset preset) {
    var user = ServiceUtil.getPrincipal();
    return createStsCredentialsForUser(preset, user);
  }

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForAnonymous() {
    var user = AuthorizedUser.createAnonymousUser(ServiceUtil.fetchPrincipalIp().orElseThrow(() -> ServiceException.of("STS: [IP] 异常！")));
    return createStsCredentialsForUser(OssRamPolicyGenerator.Preset.Anonymous, user);
  }

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsByPolicy(Policy policy, AuthorizedUser user) {
    var optionalSts = aliyunStsService.createSts(
        user, policy
    );

    if (optionalSts.isSuccess()) {
      return ServiceResult.success(
          Sts.Credentials.of(optionalSts.guard().getCredentials())
      );
    }

    return ServiceResult.error("授权失败！");
  }
}                                                                                                                                    
package o.w.o.resource.integration.aliyun.common.service.impl;

import o.w.o.resource.integration.aliyun.common.domain.Policy;
import o.w.o.resource.integration.aliyun.common.domain.Sts;
import o.w.o.resource.integration.aliyun.common.factory.PolicyFactory;
import o.w.o.resource.integration.aliyun.common.repository.AliyunStsRepository;
import o.w.o.resource.integration.aliyun.common.service.AliyunStsService;
import o.w.o.resource.integration.aliyun.common.util.StsHelper;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import o.w.o.server.io.service.ServiceContext;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Service;

@Service
public class AliyunStsServiceImpl implements AliyunStsService {
  final private AliyunStsRepository aliyunStsRepository;
  final private StsHelper stsHelper;

  public AliyunStsServiceImpl(AliyunStsRepository aliyunStsRepository, StsHelper stsHelper) {
    this.aliyunStsRepository = aliyunStsRepository;
    this.stsHelper = stsHelper;
  }

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset, AuthorizedUser user) {
    var optionalSts = aliyunStsRepository.createSts(
        stsHelper.generateStsSessionName(user),
        stsHelper.generateStsPolicy(PolicyFactory.of(preset, user))
    );

    if (optionalSts.isPresent()) {
      return ServiceResult.success(
          Sts.Credentials.of(optionalSts.get().getCredentials())
      );
    }

    return ServiceResult.error("授权失败！");
  }


  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset) {
    var user = ServiceContext.getAuthorizedUserFormSecurityContext();
    return createStsCredentialsForUser(preset, user);
  }

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForAnonymous() {
    var user = AuthorizedUser.anonymousUser(ServiceContext.fetchIpFromRequestContext().orElseThrow(() -> ServiceException.of("STS: [IP] 异常！")));
    return createStsCredentialsForUser(PolicyFactory.Preset.Anonymous, user);
  }

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsByPolicy(Policy policy, AuthorizedUser user) {
    var optionalSts = aliyunStsRepository.createSts(
        stsHelper.generateStsSessionName(user),
        stsHelper.generateStsPolicy(policy)
    );

    if (optionalSts.isPresent()) {
      return ServiceResult.success(
          Sts.Credentials.of(optionalSts.get().getCredentials())
      );
    }

    return ServiceResult.error("授权失败！");
  }
}                                                                                                                                    
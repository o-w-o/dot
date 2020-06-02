package ink.o.w.o.resource.integration.aliyun.service.impl;

import ink.o.w.o.resource.integration.aliyun.domain.Policy;
import ink.o.w.o.resource.integration.aliyun.domain.Sts;
import ink.o.w.o.resource.integration.aliyun.factory.PolicyFactory;
import ink.o.w.o.resource.integration.aliyun.repository.AliyunStsRepository;
import ink.o.w.o.resource.integration.aliyun.service.AliyunStsService;
import ink.o.w.o.resource.integration.aliyun.util.StsHelper;
import ink.o.w.o.resource.system.authorization.domain.AuthorizedUser;
import ink.o.w.o.server.io.service.ServiceContext;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.server.io.service.ServiceResult;
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
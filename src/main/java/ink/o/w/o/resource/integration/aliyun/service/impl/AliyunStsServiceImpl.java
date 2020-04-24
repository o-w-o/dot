package ink.o.w.o.resource.integration.aliyun.service.impl;

import ink.o.w.o.resource.integration.aliyun.domain.Policy;
import ink.o.w.o.resource.integration.aliyun.domain.Sts;
import ink.o.w.o.resource.integration.aliyun.repository.AliyunStsRepository;
import ink.o.w.o.resource.integration.aliyun.service.AliyunStsService;
import ink.o.w.o.resource.integration.aliyun.factory.PolicyFactory;
import ink.o.w.o.resource.integration.aliyun.util.StsHelper;
import ink.o.w.o.resource.system.authorization.domain.AuthorizedUser;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceResultFactory;
import ink.o.w.o.util.ServerHelper;
import org.springframework.stereotype.Service;

@Service
public class AliyunStsServiceImpl implements AliyunStsService {
  final private AliyunStsRepository aliyunStsRepository;
  final private StsHelper stsHelper;
  final private ServerHelper serverHelper;

  public AliyunStsServiceImpl(AliyunStsRepository aliyunStsRepository, StsHelper stsHelper, ServerHelper serverHelper) {
    this.aliyunStsRepository = aliyunStsRepository;
    this.stsHelper = stsHelper;
    this.serverHelper = serverHelper;
  }

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset, AuthorizedUser user) {
    var optionalSts = aliyunStsRepository.createSts(
        stsHelper.generateStsSessionName(user),
        stsHelper.generateStsPolicy(PolicyFactory.of(preset, user))
    );

    if (optionalSts.isPresent()) {
      return ServiceResultFactory.success(
          Sts.Credentials.of(optionalSts.get().getCredentials())
      );
    }

    return ServiceResultFactory.error("授权失败！");
  }


  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset) {
    var user = serverHelper.getAuthorizedUserFormSecurityContext();
    return createStsCredentialsForUser(preset, user);
  }

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsForAnonymous() {
    return createStsCredentialsForUser(PolicyFactory.Preset.Anonymous);
  }

  @Override
  public ServiceResult<Sts.Credentials> createStsCredentialsByPolicy(Policy policy, AuthorizedUser user) {
    var optionalSts = aliyunStsRepository.createSts(
        stsHelper.generateStsSessionName(user),
        stsHelper.generateStsPolicy(policy)
    );

    if (optionalSts.isPresent()) {
      return ServiceResultFactory.success(
          Sts.Credentials.of(optionalSts.get().getCredentials())
      );
    }

    return ServiceResultFactory.error("授权失败！");
  }
}

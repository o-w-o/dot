package o.w.o.resource.integration.aliyun.service;

import o.w.o.resource.integration.aliyun.factory.PolicyFactory;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.integration.aliyun.domain.Policy;
import o.w.o.resource.integration.aliyun.domain.Sts;

public interface AliyunStsService {
  ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset);

  ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset, AuthorizedUser user);

  ServiceResult<Sts.Credentials> createStsCredentialsForAnonymous();

  ServiceResult<Sts.Credentials> createStsCredentialsByPolicy(Policy policy, AuthorizedUser user);
}

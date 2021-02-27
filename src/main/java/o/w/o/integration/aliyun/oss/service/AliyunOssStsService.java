package o.w.o.integration.aliyun.oss.service;

import o.w.o.integration.aliyun.sts.domain.Policy;
import o.w.o.integration.aliyun.sts.domain.Sts;
import o.w.o.integration.aliyun.oss.domain.sts.OssRamPolicyGenerator;
import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.infrastructure.definition.ServiceResult;

public interface AliyunOssStsService {
  ServiceResult<Sts.Credentials> createStsCredentialsForUser(OssRamPolicyGenerator.Preset preset);

  ServiceResult<Sts.Credentials> createStsCredentialsForUser(OssRamPolicyGenerator.Preset preset, AuthorizedUser user);

  ServiceResult<Sts.Credentials> createStsCredentialsForAnonymous();

  ServiceResult<Sts.Credentials> createStsCredentialsByPolicy(Policy policy, AuthorizedUser user);
}

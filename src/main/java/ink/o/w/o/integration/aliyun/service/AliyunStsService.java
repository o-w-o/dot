package ink.o.w.o.integration.aliyun.service;

import ink.o.w.o.integration.aliyun.domain.Sts;
import ink.o.w.o.integration.aliyun.factory.PolicyFactory;
import ink.o.w.o.resource.authorization.domain.AuthorizedUser;
import ink.o.w.o.server.domain.ServiceResult;

public interface AliyunStsService {
  ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset);

  ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset, AuthorizedUser user);

  ServiceResult<Sts.Credentials> createStsCredentialsForAnonymous();
}

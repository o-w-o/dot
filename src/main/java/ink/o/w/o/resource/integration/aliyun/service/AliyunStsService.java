package ink.o.w.o.resource.integration.aliyun.service;

import ink.o.w.o.resource.integration.aliyun.domain.Sts;
import ink.o.w.o.resource.integration.aliyun.factory.PolicyFactory;
import ink.o.w.o.resource.system.authorization.domain.AuthorizedUser;
import ink.o.w.o.server.io.service.ServiceResult;

public interface AliyunStsService {
  ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset);

  ServiceResult<Sts.Credentials> createStsCredentialsForUser(PolicyFactory.Preset preset, AuthorizedUser user);

  ServiceResult<Sts.Credentials> createStsCredentialsForAnonymous();
}

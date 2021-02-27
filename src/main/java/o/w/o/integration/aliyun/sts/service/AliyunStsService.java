package o.w.o.integration.aliyun.sts.service;

import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.infrastructure.definition.ServiceResult;
import o.w.o.integration.aliyun.sts.domain.Policy;
import o.w.o.integration.aliyun.sts.domain.Sts;

public interface AliyunStsService {
  ServiceResult<Sts> createSts(AuthorizedUser user, Policy policy);
}

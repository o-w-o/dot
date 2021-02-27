package o.w.o.integration.aliyun.sts.service.impl;

import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.infrastructure.definition.ServiceResult;
import o.w.o.integration.aliyun.sts.domain.Policy;
import o.w.o.integration.aliyun.sts.domain.Sts;
import o.w.o.integration.aliyun.sts.repository.AliyunStsRepository;
import o.w.o.integration.aliyun.sts.service.AliyunStsService;
import o.w.o.integration.aliyun.sts.util.StsHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AliyunStsServiceImpl implements AliyunStsService {
  @Resource
  private AliyunStsRepository aliyunStsRepository;
  @Resource
  private StsHelper stsHelper;

  @Override
  public ServiceResult<Sts> createSts(AuthorizedUser user, Policy policy) {
    var optionalSts = aliyunStsRepository.createSts(
        stsHelper.generateStsSessionName(user),
        stsHelper.generateStsPolicy(policy)
    );

    if (optionalSts.isPresent()) {
      return ServiceResult.success(
          optionalSts.get()
      );
    }

    return ServiceResult.error("授权失败！");
  }
}

package o.w.o.resource.integration.aliyun.core.service.impl;

import o.w.o.resource.integration.aliyun.core.repository.AliyunOssRepository;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.integration.aliyun.core.domain.oss.TemporalOssResource;
import o.w.o.resource.integration.aliyun.core.domain.oss.UploadedOssResource;
import o.w.o.resource.integration.aliyun.core.service.AliyunOssService;
import o.w.o.server.io.service.ServiceContext;
import o.w.o.server.io.service.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class AliyunOssServiceImpl implements AliyunOssService {

  @Resource
  private AliyunOssRepository aliyunOssRepository;

  @Override
  public ServiceResult<UploadedOssResource> uploadTemporalResourceToOss(File temporalFile) {
    return ServiceResult.success(
        aliyunOssRepository.upload(
            TemporalOssResource.builder()
                .authorizedUploader(
                    ServiceContext.fetchAuthorizedUserFormSecurityContext()
                        .orElse(ServiceContext.fetchAnonymousUserFormSecurityContext().orElseThrow(ServiceException.of("获取匿名用户异常！")))
                )
                .file(temporalFile)
                .status(false)
                .build()
        )
    );
  }

  @Override
  public ServiceResult<Boolean> activateTemporalResourceOnOss(File resourceFile) {
    return null;
  }
}

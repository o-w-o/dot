package ink.o.w.o.resource.integration.aliyun.service.impl;

import ink.o.w.o.resource.integration.aliyun.domain.oss.TemporalOssResource;
import ink.o.w.o.resource.integration.aliyun.domain.oss.UploadedOssResource;
import ink.o.w.o.resource.integration.aliyun.repository.AliyunOssRepository;
import ink.o.w.o.resource.integration.aliyun.service.AliyunOssService;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.util.ContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AliyunOssServiceImpl implements AliyunOssService {
  @Autowired
  AliyunOssRepository aliyunOssRepository;

  @Override
  public ServiceResult<UploadedOssResource> uploadTemporalResourceToOss(File temporalFile) {
    return ServiceResult.success(
        aliyunOssRepository.upload(
            TemporalOssResource.builder()
                .authorizedUploader(
                    ContextHelper.fetchAuthorizedUserFormSecurityContext()
                        .orElse(ContextHelper.fetchAnonymousUserFormSecurityContext().orElseThrow(ServiceException.of("获取匿名用户异常！")))
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

package ink.o.w.o.resource.integration.aliyun.service.impl;

import ink.o.w.o.resource.integration.aliyun.domain.oss.TemporalOssResource;
import ink.o.w.o.resource.integration.aliyun.domain.oss.UploadedOssResource;
import ink.o.w.o.resource.integration.aliyun.repository.AliyunOssRepository;
import ink.o.w.o.resource.integration.aliyun.service.AliyunOssService;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceResultFactory;
import ink.o.w.o.util.ServerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AliyunOssServiceImpl implements AliyunOssService {
  @Autowired
  AliyunOssRepository aliyunOssRepository;

  @Autowired
  ServerHelper serverHelper;

  @Override
  public ServiceResult<UploadedOssResource> uploadTemporalResourceToOss(File temporalFile) {
    return ServiceResultFactory.success(
        aliyunOssRepository.upload(
            TemporalOssResource.builder()
                .authorizedUploader(serverHelper.getAuthorizedUserFormSecurityContext())
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

package ink.o.w.o.resource.integration.aliyun.service;

import ink.o.w.o.resource.integration.aliyun.domain.oss.UploadedOssResource;
import ink.o.w.o.server.io.service.ServiceResult;

import java.io.File;

public interface AliyunOssService {
  ServiceResult<UploadedOssResource> uploadTemporalResourceToOss(File temporalFile);

  ServiceResult<Boolean> activateTemporalResourceOnOss(File resourceFile);
}

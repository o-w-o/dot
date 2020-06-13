package o.w.o.resource.integration.aliyun.service;

import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.integration.aliyun.domain.oss.UploadedOssResource;

import java.io.File;

public interface AliyunOssService {
  ServiceResult<UploadedOssResource> uploadTemporalResourceToOss(File temporalFile);

  ServiceResult<Boolean> activateTemporalResourceOnOss(File resourceFile);
}

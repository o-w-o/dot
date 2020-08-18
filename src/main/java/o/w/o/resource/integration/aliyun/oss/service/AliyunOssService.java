package o.w.o.resource.integration.aliyun.oss.service;

import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.integration.aliyun.oss.domain.StagedResource;

import java.io.File;

public interface AliyunOssService {
  ServiceResult<StagedResource> stageResourceToOss(File temporalFile);

  ServiceResult<ResourceSpace> persistResourceOnOss(ResourceSpace resourceSpace);
}

package o.w.o.integration.aliyun.oss.service;

import o.w.o.domain.core.storage.domian.FileStorage;
import o.w.o.infrastructure.definition.ServiceResult;
import o.w.o.integration.aliyun.oss.domain.StagedResource;

import java.io.File;

public interface AliyunOssService {
  ServiceResult<StagedResource> stageResourceToOss(File temporalFile);

  ServiceResult<FileStorage> persistResourceOnOss(FileStorage resourceSpace);

  ServiceResult<File> fetchResourceOnOss(String path);
  ServiceResult<Boolean> loadTemplateResourceOnOss(String path, String templatePath);
}

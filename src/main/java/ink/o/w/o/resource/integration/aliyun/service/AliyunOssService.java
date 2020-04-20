package ink.o.w.o.resource.integration.aliyun.service;

import ink.o.w.o.server.io.service.ServiceResult;

import java.io.File;

public interface AliyunOssService {
  ServiceResult<Boolean> uploadAvatarToOss(File avatarFile);

  ServiceResult<Boolean> uploadResourceToOss(File resourceFile);
}

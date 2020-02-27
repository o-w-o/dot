package ink.o.w.o.resource.aliyun.service;

import ink.o.w.o.server.domain.ServiceResult;

import java.io.File;

public interface AliyunOssService {
  ServiceResult<Boolean> uploadAvatarToOss(File avatarFile);

  ServiceResult<Boolean> uploadResourceToOss(File resourceFile);
}

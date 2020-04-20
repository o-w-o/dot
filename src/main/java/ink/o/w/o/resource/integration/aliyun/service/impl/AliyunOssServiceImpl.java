package ink.o.w.o.resource.integration.aliyun.service.impl;

import ink.o.w.o.resource.integration.aliyun.service.AliyunOssService;
import ink.o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AliyunOssServiceImpl implements AliyunOssService {
  @Override
  public ServiceResult<Boolean> uploadAvatarToOss(File avatarFile) {
    return null;
  }

  @Override
  public ServiceResult<Boolean> uploadResourceToOss(File resourceFile) {
    return null;
  }
}

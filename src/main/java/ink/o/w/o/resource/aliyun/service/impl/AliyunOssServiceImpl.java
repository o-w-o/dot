package ink.o.w.o.resource.aliyun.service.impl;

import ink.o.w.o.resource.aliyun.service.AliyunOssService;
import ink.o.w.o.server.domain.ServiceResult;
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

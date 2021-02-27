package o.w.o.integration.aliyun.oss.service.impl;

import o.w.o.domain.core.storage.domian.FileStorage;
import o.w.o.domain.core.storage.properties.MyStoreProperties;
import o.w.o.domain.core.storage.service.FileStorageService;
import o.w.o.infrastructure.definition.ServiceException;
import o.w.o.infrastructure.definition.ServiceResult;
import o.w.o.integration.aliyun.oss.service.AliyunOssService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static o.w.o.domain.core.storage.properties.MyStoreProperties.StoreEnv.OSS_ALIYUN;
import static o.w.o.domain.core.storage.properties.MyStoreProperties.StorePropertyKey.ENV;

@Service
@ConditionalOnProperty(prefix = MyStoreProperties.PREFIX, name = ENV, havingValue = OSS_ALIYUN)
public class AliyunOssFileStorageServiceImpl implements FileStorageService {
  @Resource
  private AliyunOssService aliyunOssService;

  @Override
  public ServiceResult<FileStorage> stage(FileStorage resourceSpace) {
    var res = aliyunOssService.stageResourceToOss(resourceSpace.getFile()).guard();
    if (res.getStatus()) {
      resourceSpace
          .setUrl(res.getUrl())
          .setUrn(res.getUrn())
          .setStage(FileStorage.Stage.STAGED)
          .setVisibility(FileStorage.Visibility.TRANSIT);

      return ServiceResult.success(resourceSpace);
    }
    return ServiceResult.error(res.getMessage());
  }

  @Override
  public ServiceResult<FileStorage> persist(FileStorage resourceSpace) {
    return aliyunOssService.persistResourceOnOss(resourceSpace);
  }

  @Override
  public ServiceResult<Boolean> move(FileStorage resourceSpace) {
    throw ServiceException.unsupported();
  }

  @Override
  public ServiceResult<Boolean> changeVisible(FileStorage resourceSpace) {
    throw ServiceException.unsupported();
  }

  @Override
  public ServiceResult<Boolean> copy(FileStorage resourceSpace) {
    throw ServiceException.unsupported();
  }

  @Override
  public ServiceResult<Boolean> remove(FileStorage resourceSpace) {
    throw ServiceException.unsupported();
  }

  @Override
  public ServiceResult<Boolean> loadTemplateResource(String path, String templatePath) {
    return aliyunOssService.loadTemplateResourceOnOss(path, templatePath);
  }
}

package o.w.o.integration.aliyun.oss.service.impl;

import cn.hutool.core.io.FileUtil;
import o.w.o.domain.core.storage.domian.FileStorage;
import o.w.o.infrastructure.definition.ServiceResult;
import o.w.o.infrastructure.util.ServiceUtil;
import o.w.o.integration.aliyun.oss.domain.MovingResource;
import o.w.o.integration.aliyun.oss.domain.StagedResource;
import o.w.o.integration.aliyun.oss.domain.StagingResource;
import o.w.o.integration.aliyun.oss.repository.AliyunOssRepository;
import o.w.o.integration.aliyun.oss.service.AliyunOssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

import static o.w.o.domain.core.storage.util.FileStorageUtil.getActualPath;
import static o.w.o.domain.core.storage.util.FileStorageUtil.getTemporalPath;

@Service
@CacheConfig(cacheNames = "tpl")
public class AliyunOssServiceImpl implements AliyunOssService {


  @Value("${my.store.dir}")
  private String storageDir;
  @Value("${spring.mustache.prefix}")
  private String templateDir;

  @Resource
  private AliyunOssRepository aliyunOssRepository;

  @Override
  public ServiceResult<StagedResource> stageResourceToOss(File storedFile) {
    return ServiceResult.success(
        aliyunOssRepository.stage(
            StagingResource.builder()
                .authorizedUploader(
                    ServiceUtil.getPrincipal()
                )
                .file(storedFile)
                .stagePath(getTemporalPath(storedFile.getName()))
                .build()
        )
    );
  }

  @Override
  public ServiceResult<FileStorage> persistResourceOnOss(FileStorage resourceSpace) {
    var res = aliyunOssRepository.move(
        MovingResource.builder()
            .resourceSpace(resourceSpace)
            .targetPath(getActualPath(resourceSpace))
            .originalPath(getTemporalPath(String.format("%s.%s", resourceSpace.getName(), resourceSpace.getSuffix())))
            .stage(FileStorage.Stage.PERSISTING)
            .build()
    );

    if (res.getStatus()) {
      return ServiceResult.success(res.getFileStorage());
    }

    return ServiceResult.error(res.getMessage());
  }

  @Override
  public ServiceResult<File> fetchResourceOnOss(String path) {
    return ServiceResult.success(aliyunOssRepository.download(path));
  }

  @Override
  @Cacheable(key = "'load:#' + #templatePath", condition = "#result?.isSuccess()", sync = true)
  public ServiceResult<Boolean> loadTemplateResourceOnOss(String path, String templatePath) {
    try {
      var templateFile = new ClassPathResource("/templates/" + templatePath).getFile();
      var ossFile = aliyunOssRepository.download(path);

      FileUtil.copy(ossFile, templateFile, true);

      return ServiceResult.success();
    } catch (IOException e) {
      return ServiceResult.error(e.getMessage());
    }
  }
}

package o.w.o.resource.integration.aliyun.oss.service.impl;

import o.w.o.resource.integration.aliyun.oss.domain.MovingResource;
import o.w.o.resource.integration.aliyun.oss.domain.StagedResource;
import o.w.o.resource.integration.aliyun.oss.domain.StagingResource;
import o.w.o.resource.integration.aliyun.oss.repository.AliyunOssRepository;
import o.w.o.resource.integration.aliyun.oss.service.AliyunOssService;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.server.io.service.ServiceContext;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

import static o.w.o.resource.symbols.field.util.ResourceFieldSpaceUtil.getActualPath;
import static o.w.o.resource.symbols.field.util.ResourceFieldSpaceUtil.getTemporalPath;

@Service
public class AliyunOssServiceImpl implements AliyunOssService {

  @Resource
  private AliyunOssRepository aliyunOssRepository;

  @Override
  public ServiceResult<StagedResource> stageResourceToOss(File storedFile) {
    return ServiceResult.success(
        aliyunOssRepository.stage(
            StagingResource.builder()
                .authorizedUploader(
                    ServiceContext.getAuthorizedUserFormSecurityContext()
                )
                .file(storedFile)
                .stagePath(getTemporalPath(storedFile.getName()))
                .build()
        )
    );
  }

  @Override
  public ServiceResult<ResourceSpace> persistResourceOnOss(ResourceSpace resourceSpace) {
    var res = aliyunOssRepository.move(
        MovingResource.builder()
            .resourceSpace(resourceSpace)
            .targetPath(getActualPath(resourceSpace))
            .originalPath(getTemporalPath(String.format("%s.%s", resourceSpace.getName(), resourceSpace.getSuffix())))
            .stage(ResourceSpace.Stage.PERSISTING)
            .build()
    );

    if (res.getStatus()) {
      return ServiceResult.success(res.getResourceSpace());
    }

    return ServiceResult.error(res.getMessage());
  }
}

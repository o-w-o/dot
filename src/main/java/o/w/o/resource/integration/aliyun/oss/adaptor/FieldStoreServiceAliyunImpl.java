package o.w.o.resource.integration.aliyun.oss.adaptor;

import o.w.o.resource.integration.aliyun.oss.service.AliyunOssService;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.resource.symbols.field.service.FieldService;
import o.w.o.resource.symbols.field.util.ResourceFieldSpaceUtil;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@ConditionalOnProperty(prefix = "my.store", name = "type", havingValue = "oss", matchIfMissing = true)
public class FieldStoreServiceAliyunImpl implements FieldService.FieldStoreService {
  @Resource
  private AliyunOssService aliyunOssService;

  @Override
  public ServiceResult<ResourceSpace> stage(ResourceSpace resourceSpace) {
    var res = aliyunOssService.stageResourceToOss(resourceSpace.getFile()).guard();
    if (res.getStatus()) {
      var payload = ResourceFieldSpaceUtil.generateResourceSpacePayload(resourceSpace);
      resourceSpace.setPayloadContent(payload);
      resourceSpace.setPayloadType(payload.getPayloadType());
      resourceSpace
          .setUrl(res.getUrl())
          .setUrn(res.getUrn())
          .setStage(ResourceSpace.Stage.STAGED)
          .setVisibility(ResourceSpace.Visibility.TRANSIT);


      return ServiceResult.success(resourceSpace);
    }

    return ServiceResult.error(res.getMessage());
  }

  @Override
  public ServiceResult<ResourceSpace> persist(ResourceSpace resourceSpace) {
    return aliyunOssService.persistResourceOnOss(resourceSpace);
  }

  @Override
  public ServiceResult<Boolean> move(ResourceSpace resourceSpace) {
    throw ServiceException.unsupport();
  }

  @Override
  public ServiceResult<Boolean> changeVisible(ResourceSpace resourceSpace) {
    throw ServiceException.unsupport();
  }

  @Override
  public ServiceResult<Boolean> copy(ResourceSpace resourceSpace) {
    throw ServiceException.unsupport();
  }

  @Override
  public ServiceResult<Boolean> remove(ResourceSpace resourceSpace) {
    throw ServiceException.unsupport();
  }
}

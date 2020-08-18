package o.w.o.resource.integration.aliyun.oss.adaptor;

import o.w.o.resource.integration.aliyun.oss.service.AliyunOssService;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.resource.symbols.field.service.FieldService;
import o.w.o.resource.symbols.field.util.ResourceFieldSpaceUtil;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@ConditionalOnProperty(prefix = "my.store", name = "type", havingValue = "oss", matchIfMissing = true)
public class FieldStoreServiceAliyunImpl implements FieldService.FieldStoreService {
  @Resource
  private AliyunOssService aliyunOssService;


  private FieldService fieldService;

  @Autowired
  @Lazy
  private void setFieldService(FieldService fieldService) {
    this.fieldService = fieldService;
  }

  @Override
  public ServiceResult<ResourceSpace> storeTemporarily(ResourceSpace resourceSpace) {
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
  public ServiceResult<ResourceSpace> storePermanently(ResourceSpace resourceSpace) {
    return aliyunOssService.persistResourceOnOss(resourceSpace);
  }

  @Override
  public ServiceResult<Boolean> move(ResourceSpace resourceSpace) {
    return null;
  }

  @Override
  public ServiceResult<Boolean> changeVisible(ResourceSpace resourceSpace) {
    return null;
  }

  @Override
  public ServiceResult<Boolean> copy(ResourceSpace resourceSpace) {
    return null;
  }

  @Override
  public ServiceResult<Boolean> remove(ResourceSpace resourceSpace) {
    return null;
  }
}

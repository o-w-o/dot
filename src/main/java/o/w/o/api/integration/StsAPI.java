package o.w.o.api.integration;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.integration.aliyun.common.factory.PolicyFactory;
import o.w.o.resource.integration.aliyun.common.service.AliyunStsService;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.api.annotation.APIResourceCreate;
import o.w.o.server.io.api.annotation.APIResourceSchema;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;

@Slf4j
@APIResource(path = "ali/sts")
public class StsAPI {
  @Resource
  private AliyunStsService aliyunStsService;

  @APIResourceSchema
  public APIResult<APISchemata> fetchSchema() {
    return APIResult.of(APIContext.fetchAPIContext(StsAPI.class).orElseThrow(APIException::new));
  }

  @APIResourceCreate(path = "/somebody", name = "创建【读写】STS", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public APIResult<?> createStsSomebody() {
    var result = aliyunStsService.createStsCredentialsForUser(PolicyFactory.Preset.User_ReadAndWrite);
    return APIResult.of(result.guard());
  }

  @APIResourceCreate(path = "/", name = "创建【只读】STS", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public APIResult<?> createSts() {
    var result = aliyunStsService.createStsCredentialsForUser(PolicyFactory.Preset.User_ReadOnly);
    return APIResult.of(result.guard());
  }

  @APIResourceCreate(path = "/nobody", name = "创建【匿名】STS", produces = "application/json")
  public APIResult<?> createStsNobody() {
    return APIResult.of(aliyunStsService.createStsCredentialsForAnonymous().guard());
  }
}

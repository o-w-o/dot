package o.w.o.api;

import o.w.o.resource.core.dot.service.DotService;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.api.annotation.APIResourceCreate;
import o.w.o.server.io.api.annotation.APIResourceFetch;
import o.w.o.server.io.api.annotation.APIResourceSchema;
import o.w.o.resource.core.dot.domain.Dot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@APIResource(path = "dots")
public class DotAPI {
  @Resource
  private DotService dotService;

  @APIResourceSchema
  public APIResult<APISchemata> schema() {
    return APIResult.of(APIContext.fetchAPIContext(DotAPI.class).orElseThrow(APIException::new));
  }

  @APIResourceCreate(name = "创建 Dot")
  public APIResult<Dot> create(@RequestBody @Valid Dot dot) {
    return APIResult.of(dotService.create(dot).guard());
  }

  @APIResourceFetch(path = "/{id}", name = "根据 Id 获取 Dot")
  public APIResult<Dot> retrieve(@PathVariable("id") String dotId) {
    return APIResult.of(dotService.retrieve(dotId).guard());
  }
}

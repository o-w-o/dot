package ink.o.w.o.api;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.service.DotService;
import ink.o.w.o.server.io.api.APISchemata;
import ink.o.w.o.server.io.api.annotation.*;
import ink.o.w.o.server.io.api.APIException;
import ink.o.w.o.server.io.api.APIResult;
import ink.o.w.o.util.ContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Slf4j
@APIResource(path = "dots")
public class DotAPI {
  private final DotService dotService;

  @Autowired
  public DotAPI(DotService dotService) {
    this.dotService = dotService;
  }

  @APIResourceSchema
  public APIResult<APISchemata> schema() {
    return APIResult.of(ContextHelper.fetchAPIContext(DotAPI.class).orElseThrow(APIException::new));
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

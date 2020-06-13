package ink.o.w.o.resource.core.dot.service.handler;

import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.repository.DotRepository;
import ink.o.w.o.resource.core.dot.repository.DotSpaceRepository;
import ink.o.w.o.server.io.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class DotSpaceHandlerFactory<T extends DotSpace> {
  @Resource
  private JsonHelper jsonHelper;
  @Resource
  private DotRepository dotRepository;

  public DotSpaceDefaultHandler<T> getDefaultHandler(DotSpaceRepository<T> dotSpaceRepository) {
    return new DotSpaceDefaultHandler<T>()
        .setJsonHelper(jsonHelper)
        .setDotRepository(dotRepository)
        .setDotSpaceRepository(dotSpaceRepository);
  }
}

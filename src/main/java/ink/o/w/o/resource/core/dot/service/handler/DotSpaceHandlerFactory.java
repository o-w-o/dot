package ink.o.w.o.resource.core.dot.service.handler;

import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.repository.DotRepository;
import ink.o.w.o.resource.core.dot.repository.DotSpaceRepository;
import ink.o.w.o.server.io.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DotSpaceHandlerFactory<T extends DotSpace> {
  @Autowired
  private JsonHelper jsonHelper;
  @Autowired
  private DotRepository dotRepository;

  public DotSpaceDefaultHandler<T> getDefaultHandler(DotSpaceRepository<T> dotSpaceRepository) {
    return new DotSpaceDefaultHandler<T>()
        .setJsonHelper(jsonHelper)
        .setDotRepository(dotRepository)
        .setDotSpaceRepository(dotSpaceRepository);
  }
}

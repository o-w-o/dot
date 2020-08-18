package o.w.o.resource.symbols.field.service.handler;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.resource.symbols.field.repository.FieldRepository;
import o.w.o.resource.symbols.field.repository.FieldSpaceRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FieldSpaceHandlerFactory<T extends FieldSpace> {
  @Resource
  private FieldRepository fieldRepository;

  public FieldSpaceDefaultHandler<T> getDefaultHandler(FieldSpaceRepository<T> fieldSpaceRepository) {
    return new FieldSpaceDefaultHandler<T>()
        .setFieldRepository(fieldRepository)
        .setFieldSpaceRepository(fieldSpaceRepository);
  }
}

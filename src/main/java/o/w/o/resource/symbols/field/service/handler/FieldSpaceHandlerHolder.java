package o.w.o.resource.symbols.field.service.handler;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.FieldType;
import o.w.o.server.io.handler.EntityHandlerHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FieldSpaceHandlerHolder extends EntityHandlerHolder<FieldType.TypeEnum, FieldSpaceHandler, FieldTypeSelector> {
  public FieldSpaceHandlerHolder() {
    super(FieldSpaceHandler.class, FieldTypeSelector.class);
  }
}

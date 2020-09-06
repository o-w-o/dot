package o.w.o.resource.symbols.field.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.ext.ElementSpacePayloadType;
import o.w.o.server.io.handler.EntityHandlerHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ElementFieldSpacePayloadHandlerHolder extends EntityHandlerHolder<ElementSpacePayloadType.TypeEnum, ElementFieldSpacePayloadHandler, ElementFieldSpacePayloadHandler.TypeSelector> {
  public ElementFieldSpacePayloadHandlerHolder() {
    super(ElementFieldSpacePayloadHandler.class, ElementFieldSpacePayloadHandler.TypeSelector.class, ElementFieldSpacePayloadHandler.DEFAULT_HANDLER);
  }
}

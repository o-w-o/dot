package o.w.o.resource.symbols.field.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpacePayloadType;
import o.w.o.server.io.handler.EntityHandlerHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResourceFieldSpacePayloadHandlerHolder extends EntityHandlerHolder<ResourceSpacePayloadType.TypeEnum, ResourceFieldSpacePayloadHandler, ResourceFieldSpacePayloadHandler.TypeSelector> {
  public ResourceFieldSpacePayloadHandlerHolder() {
    super(ResourceFieldSpacePayloadHandler.class, ResourceFieldSpacePayloadHandler.TypeSelector.class, ResourceFieldSpacePayloadHandler.DEFAULT_HANDLER);
  }
}

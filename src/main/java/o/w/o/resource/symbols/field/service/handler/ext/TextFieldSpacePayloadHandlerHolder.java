package o.w.o.resource.symbols.field.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.ext.TextSpacePayloadType;
import o.w.o.server.io.handler.EntityHandlerHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TextFieldSpacePayloadHandlerHolder extends EntityHandlerHolder<TextSpacePayloadType.TypeEnum, TextFieldSpacePayloadHandler, TextFieldSpacePayloadHandler.TypeSelector> {
  public TextFieldSpacePayloadHandlerHolder() {
    super(TextFieldSpacePayloadHandler.class, TextFieldSpacePayloadHandler.TypeSelector.class, TextFieldSpacePayloadHandler.DEFAULT_HANDLER);
  }
}

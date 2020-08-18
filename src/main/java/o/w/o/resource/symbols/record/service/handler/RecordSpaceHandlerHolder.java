package o.w.o.resource.symbols.record.service.handler;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.record.domain.RecordType;
import o.w.o.server.io.handler.EntityHandlerHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecordSpaceHandlerHolder extends EntityHandlerHolder<RecordType.TypeEnum, RecordSpaceHandler, RecordTypeSelector> {
  public RecordSpaceHandlerHolder() {
    super(RecordSpaceHandler.class, RecordTypeSelector.class);
  }
}

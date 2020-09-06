package o.w.o.resource.symbols.field.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.ext.ElementSpace;
import o.w.o.resource.symbols.field.domain.ext.ElementSpacePayload;
import o.w.o.resource.symbols.field.domain.ext.ElementSpacePayloadType;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Slf4j
@Component
public class ElementFieldSpacePayloadHandler {
  protected static final ElementFieldSpacePayloadHandler DEFAULT_HANDLER = new ElementFieldSpacePayloadHandler();

  public ServiceResult<ElementSpacePayload> validate(ElementSpace elementSpace) {
    throw ServiceException.unsupport();
  }

  public ServiceResult<ElementSpacePayload> process(ElementSpace elementSpace) {
    throw ServiceException.unsupport();
  }

  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface TypeSelector {
    ElementSpacePayloadType.TypeEnum value();
  }

  @Component
  @TypeSelector(ElementSpacePayloadType.TypeEnum.PARAGRAPH)
  public static class Paragraph extends ElementFieldSpacePayloadHandler {
    @Override
    public ServiceResult<ElementSpacePayload> validate(ElementSpace elementSpace) {
      return ServiceResult.success(elementSpace.getPayload());
    }
  }
}

package o.w.o.resource.symbols.field.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.ext.TextSpace;
import o.w.o.resource.symbols.field.domain.ext.TextSpacePayload;
import o.w.o.resource.symbols.field.domain.ext.TextSpacePayloadType;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Slf4j
@Component
public class TextFieldSpacePayloadHandler {
  protected static final TextFieldSpacePayloadHandler DEFAULT_HANDLER = new TextFieldSpacePayloadHandler();

  public ServiceResult<TextSpacePayload> validate(TextSpace textSpace) {
    throw ServiceException.unsupport();
  }

  public ServiceResult<TextSpacePayload> process(TextSpace textSpace) {
    throw ServiceException.unsupport();
  }

  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface TypeSelector {
    TextSpacePayloadType.TypeEnum value();
  }

  @Component
  @TypeSelector(TextSpacePayloadType.TypeEnum.PARAGRAPH)
  public static class Paragraph extends TextFieldSpacePayloadHandler {
    @Override
    public ServiceResult<TextSpacePayload> validate(TextSpace textSpace) {
      return ServiceResult.success(textSpace.getPayload());
    }
  }
}

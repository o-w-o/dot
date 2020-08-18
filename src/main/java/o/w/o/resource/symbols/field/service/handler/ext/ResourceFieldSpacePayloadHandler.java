package o.w.o.resource.symbols.field.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpacePayload;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpacePayloadType;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Slf4j
@Component
public class ResourceFieldSpacePayloadHandler {
  protected static final ResourceFieldSpacePayloadHandler DEFAULT_HANDLER = new ResourceFieldSpacePayloadHandler();

  public ServiceResult<ResourceSpacePayload> process(ResourceSpace resourceSpace) {
    return ServiceResult.success(resourceSpace.getPayload());
  }

  public ServiceResult<ResourceSpacePayload> validate(ResourceSpace resourceSpace) {
    throw ServiceException.unsupport();
  }

  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface TypeSelector {
    ResourceSpacePayloadType.TypeEnum value();
  }

  @Component
  @TypeSelector(ResourceSpacePayloadType.TypeEnum.PICTURE)
  public static class Picture extends ResourceFieldSpacePayloadHandler {
    @Override
    public ServiceResult<ResourceSpacePayload> process(ResourceSpace resourceSpace) {
      return ServiceResult.success(resourceSpace.getPayload());
    }
  }
}

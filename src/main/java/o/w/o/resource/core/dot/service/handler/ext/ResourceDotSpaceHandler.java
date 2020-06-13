package o.w.o.resource.core.dot.service.handler.ext;

import o.w.o.resource.core.dot.repository.DotSpaceRepository;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.core.dot.domain.Dot;
import o.w.o.resource.core.dot.domain.DotSpace;
import o.w.o.resource.core.dot.domain.DotType;
import o.w.o.resource.core.dot.domain.ext.ResourceSpace;
import o.w.o.resource.core.dot.domain.ext.ResourceSpacePayload;
import o.w.o.resource.core.dot.service.handler.DotSpaceDefaultHandler;
import o.w.o.resource.core.dot.service.handler.DotSpaceHandler;
import o.w.o.resource.core.dot.service.handler.DotSpaceHandlerFactory;
import o.w.o.resource.core.dot.service.handler.DotTypeSelector;
import o.w.o.server.io.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
@DotTypeSelector(value = DotType.TypeEnum.RESOURCE)
public class ResourceDotSpaceHandler extends DotSpaceHandler {

  @Resource
  DotSpaceRepository<ResourceSpace> resourceRepository;

  @Resource
  DotSpaceHandlerFactory<ResourceSpace> dotDotSpaceHandlerFactory;
  DotSpaceDefaultHandler<ResourceSpace> dotDotSpaceDefaultHandler;

  @PostConstruct
  private void init() {
    dotDotSpaceDefaultHandler = dotDotSpaceHandlerFactory.getDefaultHandler(resourceRepository);
  }

  @Override
  public String handle(Dot dot) {
    return dotDotSpaceDefaultHandler.handle(dot);
  }

  @Override
  public ServiceResult<Dot> retrieve(String dotId, DotType dotType) {
    return dotDotSpaceDefaultHandler.fetch(dotId, dotType);
  }

  @Override
  public ServiceResult<DotSpace> retrieveSpace(String dotSpaceId, DotType dotType) {
    return dotDotSpaceDefaultHandler.fetchSpace(dotSpaceId, dotType);
  }

  @Override
  public ServiceResult<Dot> create(Dot dot) {
    var space = (ResourceSpace) dot.getSpace();
    logger.info("dot -> [{}], payload -> [{}]", space, space.getPayload());

    var type = space.getPayload().getPayloadType();
    space.setPayloadType(type);

    switch (type.getType()) {
      case PICTURE: {
        var payload = (ResourceSpacePayload.Picture) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case AUDIO: {
        var payload = (ResourceSpacePayload.Audio) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case VIDEO: {
        var payload = (ResourceSpacePayload.Video) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case TEXT: {
        var payload = (ResourceSpacePayload.Text) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case BINARY: {
        var payload = (ResourceSpacePayload.Binary) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      default: {
        throw ServiceException.of(
            String.format("未知的 ResourceSpacePayload 类型 [%s]", space.getPayloadType())
        );
      }
    }


    var createdDotSpace = resourceRepository.save(space);
    var spaceMountedDot = dot
        .setSpace(createdDotSpace)
        .setSpaceId(createdDotSpace.getId());

    spaceMountedDot.setSpaceContent(createdDotSpace);
    logger.info("spaceMountedDot -> [{}]", spaceMountedDot);

    return ServiceResult.success((Dot) spaceMountedDot);

  }
}

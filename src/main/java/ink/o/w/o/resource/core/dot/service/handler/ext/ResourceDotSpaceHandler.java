package ink.o.w.o.resource.core.dot.service.handler.ext;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.ext.ResourceSpace;
import ink.o.w.o.resource.core.dot.domain.ext.ResourceSpacePayload;
import ink.o.w.o.resource.core.dot.repository.DotSpaceRepository;
import ink.o.w.o.resource.core.dot.service.handler.DotSpaceDefaultHandler;
import ink.o.w.o.resource.core.dot.service.handler.DotSpaceHandler;
import ink.o.w.o.resource.core.dot.service.handler.DotSpaceHandlerFactory;
import ink.o.w.o.resource.core.dot.service.handler.DotTypeSelector;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.server.io.service.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
@DotTypeSelector(value = DotType.DotTypeEnum.RESOURCE)
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

    switch (space.getPayloadType().getType()) {
      case RESOURCE_PICTURE: {
        var payload = (ResourceSpacePayload.Picture) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case RESOURCE_AUDIO: {
        var payload = (ResourceSpacePayload.Audio) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case RESOURCE_VIDEO: {
        var payload = (ResourceSpacePayload.Video) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case RESOURCE_TEXT: {
        var payload = (ResourceSpacePayload.Text) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case RESOURCE_BINARY: {
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

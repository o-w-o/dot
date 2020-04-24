package ink.o.w.o.resource.core.dot.service.handler.ext;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.ext.ResourcePictureDot;
import ink.o.w.o.resource.core.dot.repository.DotSpaceRepository;
import ink.o.w.o.resource.core.dot.service.handler.DotSpaceDefaultHandler;
import ink.o.w.o.resource.core.dot.service.handler.DotSpaceHandler;
import ink.o.w.o.resource.core.dot.service.handler.DotSpaceHandlerFactory;
import ink.o.w.o.resource.core.dot.service.handler.DotTypeSelector;
import ink.o.w.o.server.io.service.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
@DotTypeSelector(value = DotType.DotTypeEnum.RESOURCE_PICTURE)
public class ResourcePictureDotSpaceHandler extends DotSpaceHandler {

  @Resource
  DotSpaceRepository<ResourcePictureDot> resourcePictureRepository;

  @Resource
  DotSpaceHandlerFactory<ResourcePictureDot> dotDotSpaceHandlerFactory;
  DotSpaceDefaultHandler<ResourcePictureDot> dotDotSpaceDefaultHandler;

  @PostConstruct
  private void init() {
    dotDotSpaceDefaultHandler = dotDotSpaceHandlerFactory.getDefaultHandler(resourcePictureRepository);
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
    return dotDotSpaceDefaultHandler.create(dot);
  }
}

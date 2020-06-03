package ink.o.w.o.resource.core.dot.service.handler.ext;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.ext.TextSpace;
import ink.o.w.o.resource.core.dot.domain.ext.TextSpacePayload;
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
@DotTypeSelector(value = DotType.TypeEnum.TEXT)
public class TextDotSpaceHandler extends DotSpaceHandler {

  @Resource
  DotSpaceRepository<TextSpace> textRepository;

  @Resource
  DotSpaceHandlerFactory<TextSpace> dotDotSpaceHandlerFactory;
  DotSpaceDefaultHandler<TextSpace> dotDotSpaceDefaultHandler;

  @PostConstruct
  private void init() {
    dotDotSpaceDefaultHandler = dotDotSpaceHandlerFactory.getDefaultHandler(textRepository);
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
    var space = (TextSpace) dot.getSpace();
    logger.info("dot -> [{}], payload -> [{}]", space, space.getPayload());

    switch (space.getPayloadType().getType()) {
      case SUMMARY: {
        var payload = (TextSpacePayload.Summary) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case HEADING: {
        var payload = (TextSpacePayload.Heading) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case PARAGRAPH: {
        var payload = (TextSpacePayload.Paragraph) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case EMBED: {
        var payload = (TextSpacePayload.Embed) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      case LINK: {
        var payload = (TextSpacePayload.LINK) space.getPayload();
        space.setPayloadContent(payload);
        break;
      }
      default: {
        throw ServiceException.of(
            String.format("未知的 TextSpacePayload 类型 [%s]", space.getPayloadType())
        );
      }
    }


    var createdDotSpace = textRepository.save(space);
    var spaceMountedDot = dot
        .setSpace(createdDotSpace)
        .setSpaceId(createdDotSpace.getId());

    spaceMountedDot.setSpaceContent(createdDotSpace);
    logger.info("spaceMountedDot -> [{}]", spaceMountedDot);

    return ServiceResult.success((Dot) spaceMountedDot);

  }
}

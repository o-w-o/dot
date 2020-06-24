package o.w.o.resource.symbol.dot.service.handler.ext;

import o.w.o.resource.symbol.dot.repository.DotSpaceRepository;
import o.w.o.resource.symbol.dot.service.handler.DotSpaceDefaultHandler;
import o.w.o.resource.symbol.dot.service.handler.DotSpaceHandler;
import o.w.o.resource.symbol.dot.service.handler.DotSpaceHandlerFactory;
import o.w.o.resource.symbol.dot.service.handler.DotTypeSelector;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.symbol.dot.domain.Dot;
import o.w.o.resource.symbol.dot.domain.DotSpace;
import o.w.o.resource.symbol.dot.domain.DotType;
import o.w.o.resource.symbol.dot.domain.ext.TextSpace;
import o.w.o.resource.symbol.dot.domain.ext.TextSpacePayload;
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

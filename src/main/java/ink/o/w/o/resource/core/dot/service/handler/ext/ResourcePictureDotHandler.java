package ink.o.w.o.resource.core.dot.service.handler.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.ext.ResourcePictureDot;
import ink.o.w.o.resource.core.dot.repository.DotRepository;
import ink.o.w.o.resource.core.dot.repository.ResourcePictureDotRepository;
import ink.o.w.o.resource.core.dot.service.handler.DotTypeSelector;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceResultFactory;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.util.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@DotTypeSelector(value = DotType.DotTypeEnum.RESOURCE_PICTURE)
public class ResourcePictureDotHandler extends AbstractDotHandler {
  @Resource
  private JsonHelper jsonHelper;

  @Resource
  private ResourcePictureDotRepository resourcePictureDotRepository;

  @Resource
  private DotRepository dotRepository;

  @Override
  public String handle(Dot dot) {
    ResourcePictureDot resourcePictureDot = (ResourcePictureDot) dot.getSpace();
    try {
      return jsonHelper.toJsonString(resourcePictureDot);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  @Override
  public ServiceResult<Dot> fetch(String dotId, DotType dotType) {
    return ServiceResultFactory.success(
        dotRepository.findById(dotId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Dot -> id[ %s ], type[ %s ]", dotId, dotType))
        )
    );
  }

  @Override
  public ServiceResult<DotSpace> fetchSpace(String dotSpaceId, DotType dotType) {
    return ServiceResultFactory.success(
        resourcePictureDotRepository.findById(dotSpaceId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Ink -> id[ %s ], type[ %s ]", dotSpaceId, dotType))
        )
    );
  }

  @Override
  public ServiceResult<Dot> create(Dot dot) {
    var createdDotSpace = resourcePictureDotRepository.save((ResourcePictureDot) dot.getSpace());
    try {
      var spaceMountedDot = dot
          .setSpace(createdDotSpace)
          .setSpaceId(createdDotSpace.getId())
          .setSpaceContent(jsonHelper.toJsonString(createdDotSpace));
      logger.info("spaceMountedDot -> [{}]", spaceMountedDot);
      return ServiceResultFactory.success(spaceMountedDot);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return ServiceResultFactory.error(e.getMessage());
    }
  }
}

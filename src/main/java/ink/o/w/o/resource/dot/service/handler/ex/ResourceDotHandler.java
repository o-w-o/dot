package ink.o.w.o.resource.dot.service.handler.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.dot.constant.DotType;
import ink.o.w.o.resource.dot.domain.Dot;
import ink.o.w.o.resource.dot.domain.DotSpace;
import ink.o.w.o.resource.dot.domain.ext.ResourceDot;
import ink.o.w.o.resource.dot.repository.DotRepository;
import ink.o.w.o.resource.dot.repository.ResourceDotRepository;
import ink.o.w.o.resource.dot.service.handler.DotTypeSelector;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.server.exception.ServiceException;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@DotTypeSelector(value = DotType.IMAGE)
public class ResourceDotHandler extends AbstractDotHandler {
  @Resource
  private JSONHelper jsonHelper;

  @Resource
  private ResourceDotRepository resourceDotRepository;

  @Resource
  private DotRepository dotRepository;

  @Override
  public String handle(Dot dot) {
    ResourceDot resourceDot = (ResourceDot) dot.getSpace();
    try {
      return jsonHelper.toJSONString(resourceDot);
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
        resourceDotRepository.findById(dotSpaceId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Ink -> id[ %s ], type[ %s ]", dotSpaceId, dotType))
        )
    );
  }

  @Override
  public ServiceResult<Dot> create(Dot dot) {
    var createdDotSpace = resourceDotRepository.save((ResourceDot) dot.getSpace());
    try {
      var spaceMountedDot = dot
          .setSpace(createdDotSpace)
          .setSpaceId(createdDotSpace.getId())
          .setSpaceContent(jsonHelper.toJSONString(createdDotSpace));
      logger.info("spaceMountedDot -> [{}]", spaceMountedDot);
      return ServiceResultFactory.success(spaceMountedDot);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return ServiceResultFactory.error(e.getMessage());
    }
  }
}

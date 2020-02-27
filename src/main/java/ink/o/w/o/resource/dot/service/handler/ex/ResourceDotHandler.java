package ink.o.w.o.resource.dot.service.handler.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.dot.constant.DotType;
import ink.o.w.o.resource.dot.domain.DotBasic;
import ink.o.w.o.resource.dot.domain.ext.ResourceDot;
import ink.o.w.o.resource.dot.repository.ResourceDotRepository;
import ink.o.w.o.resource.dot.service.handler.DotTypeSelector;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.server.exception.ServiceException;
import ink.o.w.o.util.JSONHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@DotTypeSelector(value = DotType.IMAGE)
public class ResourceDotHandler extends AbstractDotHandler {
  @Resource
  private JSONHelper jsonHelper;

  @Resource
  private ResourceDotRepository resourceDotRepository;

  @Override
  public String handle(DotBasic dot) {
    ResourceDot resourceDot = (ResourceDot) dot;
    try {
      return jsonHelper.toJSONString(resourceDot);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  @Override
  public ServiceResult<DotBasic> fetch(String dotId, DotType dotType) {
    return ServiceResultFactory.success(
        resourceDotRepository.findById(dotId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Dot -> id[ %s ], type[ %s ]", dotId, dotType))
        )
            .dehydrate()
    );
  }

  @Override
  public ServiceResult<DotBasic> create(DotBasic dot) {
    return ServiceResultFactory.success(resourceDotRepository.save(new ResourceDot(dot)));
  }
}

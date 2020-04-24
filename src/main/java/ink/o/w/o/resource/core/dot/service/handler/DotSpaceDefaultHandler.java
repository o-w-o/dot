package ink.o.w.o.resource.core.dot.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.repository.DotRepository;
import ink.o.w.o.resource.core.dot.repository.DotSpaceRepository;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceResultFactory;
import ink.o.w.o.util.JsonHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
public class DotSpaceDefaultHandler<T extends DotSpace> {
  private JsonHelper jsonHelper;

  private DotRepository dotRepository;
  private DotSpaceRepository<T> dotSpaceRepository;

  @SuppressWarnings({"unchecked", ""})
  public String handle(Dot dot) {
    T resourcePictureDot = (T) dot.getSpace();
    try {
      return jsonHelper.toJsonString(resourcePictureDot);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  public ServiceResult<Dot> fetch(String dotId, DotType dotType) {
    return ServiceResultFactory.success(
        dotRepository.findById(dotId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Dot -> id[ %s ], type[ %s ]", dotId, dotType))
        )
    );
  }

  public ServiceResult<DotSpace> fetchSpace(String dotSpaceId, DotType dotType) {
    return ServiceResultFactory.success(
        dotSpaceRepository.findById(dotSpaceId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Dot -> id[ %s ], type[ %s ]", dotSpaceId, dotType))
        )
    );
  }

  @SuppressWarnings({"unchecked", ""})
  public ServiceResult<Dot> create(Dot dot) {
    T space = (T) dot.getSpace();
    T createdDotSpace = dotSpaceRepository.save(space);
    var spaceMountedDot = dot
        .setSpace(createdDotSpace)
        .setSpaceId(createdDotSpace.getId());

    spaceMountedDot.setSpaceContent(createdDotSpace);
    logger.info("spaceMountedDot -> [{}]", spaceMountedDot);

    return ServiceResultFactory.success(spaceMountedDot);
  }

}

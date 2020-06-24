package o.w.o.resource.symbol.dot.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import o.w.o.resource.symbol.dot.repository.DotRepository;
import o.w.o.resource.symbol.dot.repository.DotSpaceRepository;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.symbol.dot.domain.Dot;
import o.w.o.resource.symbol.dot.domain.DotSpace;
import o.w.o.resource.symbol.dot.domain.DotType;
import o.w.o.server.io.json.JsonHelper;
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
    return ServiceResult.success(
        dotRepository.findById(dotId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Dot -> id[ %s ], type[ %s ]", dotId, dotType))
        )
    );
  }

  public ServiceResult<DotSpace> fetchSpace(String dotSpaceId, DotType dotType) {
    return ServiceResult.success(
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

    return ServiceResult.success((Dot) spaceMountedDot);
  }

}

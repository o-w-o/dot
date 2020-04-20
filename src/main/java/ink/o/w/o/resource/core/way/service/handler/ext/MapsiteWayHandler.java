package ink.o.w.o.resource.core.way.service.handler.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.core.way.domain.Way;
import ink.o.w.o.resource.core.way.domain.WaySpace;
import ink.o.w.o.resource.core.way.domain.WayType;
import ink.o.w.o.resource.core.way.domain.ext.SitemapWay;
import ink.o.w.o.resource.core.way.repository.SitemapWayRepository;
import ink.o.w.o.resource.core.way.repository.WayRepository;
import ink.o.w.o.resource.core.way.service.handler.WayTypeSelector;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceResultFactory;
import ink.o.w.o.util.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@WayTypeSelector(value = WayType.WayTypeEnum.NET)
public class MapsiteWayHandler extends AbstractWayHandler {
  @Resource
  private JsonHelper jsonHelper;

  @Resource
  private SitemapWayRepository sitemapWayRepository;

  @Resource
  private WayRepository wayRepository;

  @Override
  public String handle(Way way) {
    SitemapWay articleInk = (SitemapWay) way.getSpace();
    try {
      return jsonHelper.toJsonString(articleInk);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

  @Override
  public ServiceResult<Way> fetch(String wayId, WayType.WayTypeEnum wayType) {
    var ink = wayRepository
        .findById(wayId)
        .orElseThrow(
            () -> new ServiceException(String.format("未找到 Way -> id[ %s ], type[ %s ]", wayId, wayType))
        );
    ink.setSpace(fetchSpace(ink.getSpaceId(), wayType).guard());
    return ServiceResultFactory.success(ink);
  }

  @Override
  public ServiceResult<WaySpace> fetchSpace(String waySpaceId, WayType.WayTypeEnum wayType) {
    return ServiceResultFactory.success(
        sitemapWayRepository.findById(waySpaceId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Way -> id[ %s ], type[ %s ]", waySpaceId, wayType))
        )
    );
  }

  @Override
  public ServiceResult<Way> create(Way way) {
    var createdInkSpace = sitemapWayRepository.save((SitemapWay) way.getSpace());
    try {
      var spaceMountedInk = way
          .setSpace(createdInkSpace)
          .setSpaceId(createdInkSpace.getId())
          .setSpaceContent(jsonHelper.toJsonString(createdInkSpace));
      logger.info("spaceMountedInk -> [{}]", spaceMountedInk);
      return ServiceResultFactory.success(spaceMountedInk);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return ServiceResultFactory.error(e.getMessage());
    }
  }
}

package o.w.o.resource.symbol.way.service.handler.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import o.w.o.resource.symbol.way.domain.Way;
import o.w.o.resource.symbol.way.domain.WaySpace;
import o.w.o.resource.symbol.way.domain.WayType;
import o.w.o.resource.symbol.way.repository.WayRepository;
import o.w.o.resource.symbol.way.repository.ext.SitemapWayRepository;
import o.w.o.server.io.json.JsonHelper;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import o.w.o.resource.symbol.way.domain.ext.SitemapWay;
import o.w.o.resource.symbol.way.service.handler.WayTypeSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@WayTypeSelector(value = WayType.TypeEnum.NET)
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
  public ServiceResult<Way> fetch(String wayId, WayType.TypeEnum wayType) {
    var ink = wayRepository
        .findById(wayId)
        .orElseThrow(
            () -> new ServiceException(String.format("未找到 Way -> id[ %s ], type[ %s ]", wayId, wayType))
        );
    ink.setSpace(fetchSpace(ink.getSpaceId(), wayType).guard());
    return ServiceResult.success(ink);
  }

  @Override
  public ServiceResult<WaySpace> fetchSpace(String waySpaceId, WayType.TypeEnum wayType) {
    return ServiceResult.success(
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
      return ServiceResult.success((Way) spaceMountedInk);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return ServiceResult.error(e.getMessage());
    }
  }
}

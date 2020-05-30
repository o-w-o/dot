package ink.o.w.o.resource.core.way.service.impl;

import ink.o.w.o.resource.core.way.domain.Way;
import ink.o.w.o.resource.core.way.repository.WayRepository;
import ink.o.w.o.resource.core.way.service.WayService;
import ink.o.w.o.resource.core.way.service.handler.WayHandlerHolder;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.server.io.service.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * WayServiceImpl
 *
 * @author symbols@dingtalk.com
 * @date 2020/04/21 19:50
 * @since 1.0.0
 */
@Slf4j
@Service
public class WayServiceImpl implements WayService {
  private final WayHandlerHolder wayHandlerHolder;
  private final WayRepository wayRepository;

  @Autowired
  WayServiceImpl(WayHandlerHolder wayHandlerHolder, WayRepository wayRepository) {
    this.wayHandlerHolder = wayHandlerHolder;
    this.wayRepository = wayRepository;
  }

  @Override
  public String test(Way way) {
    logger.info("处理 WAY 开始：{}", way.getType());
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<>("");

    wayHandlerHolder.select(way.getType().getType()).ifPresent(handler -> {
      result.set(handler.handle(way));
      status.set(true);
    });

    logger.info("处理 WAY 结束：{}", status.get());
    return result.get();
  }

  @Override
  public ServiceResult<Way> fetch(String wayId) {
    logger.info("处理 WAY 开始：{}", wayId);

    var way = wayRepository.findById(wayId).orElseThrow(() -> new ServiceException(String.format("Ink id[ %s ] 不存在！", wayId)));
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<Way>(null);

    wayHandlerHolder.select(way.getType().getType()).ifPresent(handler -> {
      result.set(handler.fetch(way.getId(), way.getType().getType()).guard());
      status.set(true);
    });

    logger.info("处理 WAY 结束：{}", status.get());
    return ServiceResult.success(result.get());
  }


  @Override
  public ServiceResult<Way> create(Way way) {
    logger.info("处理 WAY 开始： type -> [ {} ] [ {} ]", way.getType(), way);

    var result = new AtomicReference<ServiceResult<Way>>(null);

    wayHandlerHolder.select(way.getType().getType()).ifPresent(handler -> result.set(handler.create(way)));

    var spaceMountedInk = result.get().guard();

    wayRepository.save(spaceMountedInk);

    logger.info("处理 WAY 结束： success -> [ {} ]", result.get().getSuccess());
    return ServiceResult.success(spaceMountedInk);
  }
}

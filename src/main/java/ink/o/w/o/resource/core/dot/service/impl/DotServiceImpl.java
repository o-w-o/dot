package ink.o.w.o.resource.core.dot.service.impl;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.repository.DotRepository;
import ink.o.w.o.resource.core.dot.service.DotService;
import ink.o.w.o.resource.core.dot.service.handler.DotSpaceHandlerHolder;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * DotServiceImpl
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 12:46
 * @since 1.0.0
 */
@Slf4j
@Service
@Transactional(rollbackOn = ServiceException.class)
public class DotServiceImpl implements DotService {
  private final DotSpaceHandlerHolder dotSpaceHandlerHolder;
  private final DotRepository dotRepository;

  @Autowired
  DotServiceImpl(DotSpaceHandlerHolder dotSpaceHandlerHolder, DotRepository dotRepository) {
    this.dotSpaceHandlerHolder = dotSpaceHandlerHolder;
    this.dotRepository = dotRepository;
  }

  @Override
  public ServiceResult<Dot> retrieve(String dotId) {
    logger.info("处理 DOT 开始：{}", dotId);

    var dot = dotRepository.findById(dotId).orElseThrow(() -> new ServiceException(String.format("Dot id[ %s ] 不存在！", dotId)));
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<Dot>(null);

    dotSpaceHandlerHolder.select(dot.getType()).ifPresent(handler -> {
      result.set(handler.retrieve(dot.getId(), dot.getType()).guard());
      status.set(true);
    });

    logger.info("处理 DOT 结束：{}", status.get());
    return ServiceResult.success(result.get());
  }

  @Override
  public ServiceResult<List<Dot>> retrieve(String[] dotIds) {
    return null;
  }


  @Override
  public ServiceResult<Dot> create(Dot dot) {
    logger.info("处理 DOT 开始： type -> [ {} ]", dot.getType());

    var result = new AtomicReference<ServiceResult<Dot>>(null);

    dotSpaceHandlerHolder.select(dot.getType()).ifPresent(handler -> result.set(handler.create(dot)));

    var spaceMountedDot = result.get().guard();
    var createdDot = dotRepository.save(spaceMountedDot);

    logger.info("处理 DOT 结束： success -> [ {} ]", result.get().getSuccess());
    return ServiceResult.success(createdDot);
  }

  @Override
  public ServiceResult<Dot> update(Dot dot) {
    return null;
  }

  @Override
  public ServiceResult<Dot> delete(Dot dot) {
    return null;
  }
}

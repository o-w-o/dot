package ink.o.w.o.resource.core.dot.service.impl;

import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.repository.DotRepository;
import ink.o.w.o.resource.core.dot.service.DotService;
import ink.o.w.o.resource.core.dot.service.handler.DotHandlerHolder;
import ink.o.w.o.server.io.service.ServiceResult;
import ink.o.w.o.server.io.service.ServiceResultFactory;
import ink.o.w.o.server.io.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class DotServiceImpl implements DotService {
  private final DotHandlerHolder dotHandlerHolder;
  private final DotRepository dotRepository;

  @Autowired
  DotServiceImpl(DotHandlerHolder dotHandlerHolder, DotRepository dotRepository) {
    this.dotHandlerHolder = dotHandlerHolder;
    this.dotRepository = dotRepository;
  }

  @Override
  public String test(Dot dot) {
    logger.info("处理 DOT 开始：{}", dot.getType());
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<>("");

    dotHandlerHolder.select(dot.getType()).ifPresent(handler -> {
      result.set(handler.handle(dot));
      status.set(true);
    });

    logger.info("处理 DOT 结束：{}", status.get());
    return result.get();
  }

  @Override
  public ServiceResult<Dot> fetch(String dotId) {
    logger.info("处理 DOT 开始：{}", dotId);

    var dot = dotRepository.findById(dotId).orElseThrow(() -> new ServiceException(String.format("Dot id[ %s ] 不存在！", dotId)));
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<Dot>(null);

    dotHandlerHolder.select(dot.getType()).ifPresent(handler -> {
      result.set(handler.fetch(dot.getId(), dot.getType()).guard());
      status.set(true);
    });

    logger.info("处理 DOT 结束：{}", status.get());
    return ServiceResultFactory.success(result.get());
  }


  @Override
  public ServiceResult<Dot> create(Dot dot) {
    logger.info("处理 DOT 开始： type -> [ {} ]", dot.getType());

    var result = new AtomicReference<ServiceResult<Dot>>(null);

    dotHandlerHolder.select(dot.getType()).ifPresent(handler -> {
      result.set(handler.create(dot));
    });

    var createdDot = result.get().guard();

    dotRepository.save(
        new Dot()
            .setId(createdDot.getId())
            .setType(createdDot.getType())
    );

    logger.info("处理 DOT 结束： success -> [ {} ]", result.get().getSuccess());
    return ServiceResultFactory.success(createdDot);
  }
}

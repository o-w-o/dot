package ink.o.w.o.resource.ink.service.impl;

import ink.o.w.o.resource.ink.domain.Ink;
import ink.o.w.o.resource.ink.repository.InkRepository;
import ink.o.w.o.resource.ink.service.InkService;
import ink.o.w.o.resource.ink.service.handler.InkHandlerHolder;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.server.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * InkServiceImpl
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13 12:46
 * @since 1.0.0
 */
@Slf4j
@Service
public class InkServiceImpl implements InkService {
  private final InkHandlerHolder inkHandlerHolder;
  private final InkRepository inkRepository;

  @Autowired
  InkServiceImpl(InkHandlerHolder inkHandlerHolder, InkRepository inkRepository) {
    this.inkHandlerHolder = inkHandlerHolder;
    this.inkRepository = inkRepository;
  }

  @Override
  public String test(Ink ink) {
    logger.info("处理 INK 开始：{}", ink.getType());
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<>("");

    inkHandlerHolder.select(ink.getType()).ifPresent(handler -> {
      result.set(handler.handle(ink));
      status.set(true);
    });

    logger.info("处理 INK 结束：{}", status.get());
    return result.get();
  }

  @Override
  public ServiceResult<Ink> fetch(String inkId) {
    logger.info("处理 INK 开始：{}", inkId);

    var ink = inkRepository.findById(inkId).orElseThrow(() -> new ServiceException(String.format("Ink id[ %s ] 不存在！", inkId)));
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<Ink>(null);

    inkHandlerHolder.select(ink.getType()).ifPresent(handler -> {
      result.set(handler.fetch(ink.getId(), ink.getType()).guard());
      status.set(true);
    });

    logger.info("处理 INK 结束：{}", status.get());
    return ServiceResultFactory.success(result.get());
  }


  @Override
  public ServiceResult<Ink> create(Ink ink) {
    logger.info("处理 INK 开始： type -> [ {} ] [ {} ]", ink.getType(), ink);

    var result = new AtomicReference<ServiceResult<Ink>>(null);

    inkHandlerHolder.select(ink.getType()).ifPresent(handler -> {
      result.set(handler.create(ink));
    });

    var spaceMountedInk = result.get().guard();

    inkRepository.save(spaceMountedInk);

    logger.info("处理 INK 结束： success -> [ {} ]", result.get().getSuccess());
    return ServiceResultFactory.success(spaceMountedInk);
  }
}

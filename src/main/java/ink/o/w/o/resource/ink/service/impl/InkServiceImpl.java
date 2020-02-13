package ink.o.w.o.resource.ink.service.impl;

import ink.o.w.o.resource.ink.domain.ex.AbstractInk;
import ink.o.w.o.resource.ink.service.InkService;
import ink.o.w.o.resource.ink.service.handler.InkHandlerHolder;
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

  @Autowired
  InkServiceImpl(InkHandlerHolder inkHandlerHolder) {
    this.inkHandlerHolder = inkHandlerHolder;
  }

  @Override
  public String test(AbstractInk ink) {
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
}

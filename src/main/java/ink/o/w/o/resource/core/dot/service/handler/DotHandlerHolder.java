package ink.o.w.o.resource.core.dot.service.handler;

import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.service.handler.ext.AbstractDotHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class DotHandlerHolder implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  private Map<DotType.DotTypeEnum, AbstractDotHandler> container = new HashMap<>();

  @PostConstruct
  public void init() {
    logger.info("DotHandlerHolder register handler -> START");
    Map<String, AbstractDotHandler> dotHandlers = applicationContext.getBeansOfType(AbstractDotHandler.class);
    if (dotHandlers.isEmpty()) {
      logger.warn("DotHandlerHolder register handler -> EMPTY");
      return;
    }
    dotHandlers.forEach((beanName, dotHandler) -> {
      DotTypeSelector dotTypeSelector = dotHandler.getClass().getAnnotation(DotTypeSelector.class);
      if (dotTypeSelector == null) {
        logger.error("未知的 DotType -> beanName[{}], dotHandler.class[{}]", beanName, dotHandler.getClass());
      } else {
        logger.info("DotHandlerHolder register handler -> @[{}]", dotTypeSelector.value());
        this.container.put(dotTypeSelector.value(), dotHandler);
      }
    });
    logger.info("DotHandlerHolder register handler -> END");
  }

  public Optional<AbstractDotHandler> select(DotType dotType) {
    return Optional.ofNullable(container.get(dotType));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}

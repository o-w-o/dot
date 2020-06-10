package ink.o.w.o.resource.core.way.service.handler;

import ink.o.w.o.resource.core.way.domain.WayType;
import ink.o.w.o.resource.core.way.service.handler.ext.AbstractWayHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class WayHandlerHolder implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  private Map<WayType.TypeEnum, AbstractWayHandler> container = new HashMap<>();

  @PostConstruct
  public void init() {
    logger.info("InkHandlerHolder register handler -> START");
    Map<String, AbstractWayHandler> inkHandlers = applicationContext.getBeansOfType(AbstractWayHandler.class);
    if (inkHandlers.isEmpty()) {
      logger.warn("InkHandlerHolder register handler -> EMPTY");
      return;
    }
    inkHandlers.forEach((beanName, orderHandler) -> {
      WayTypeSelector wayTypeSelector = orderHandler.getClass().getAnnotation(WayTypeSelector.class);
      if (wayTypeSelector == null) {
        logger.error("未知的 WayType -> beanName[{}], orderHandler.class[{}]", beanName, orderHandler.getClass());
      } else {
        logger.info("InkHandlerHolder register handler -> @[{}]", wayTypeSelector.value());
        this.container.put(wayTypeSelector.value(), orderHandler);
      }
    });
    logger.info("InkHandlerHolder register handler -> END");
  }

  public Optional<AbstractWayHandler> select(WayType.TypeEnum inkType) {
    return Optional.ofNullable(container.get(inkType));
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}

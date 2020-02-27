package ink.o.w.o.resource.ink.service.handler;

import ink.o.w.o.resource.ink.constant.InkType;
import ink.o.w.o.resource.ink.service.handler.ext.AbstractInkHandler;
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
public class InkHandlerHolder implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  private Map<InkType, AbstractInkHandler> container = new HashMap<>();

  @PostConstruct
  public void init() {
    logger.info("InkHandlerHolder register handler -> START");
    Map<String, AbstractInkHandler> inkHandlers = applicationContext.getBeansOfType(AbstractInkHandler.class);
    if (inkHandlers.isEmpty()) {
      logger.warn("InkHandlerHolder register handler -> EMPTY");
      return;
    }
    inkHandlers.forEach((beanName, orderHandler) -> {
      InkTypeSelector inkTypeSelector = orderHandler.getClass().getAnnotation(InkTypeSelector.class);
      if (inkTypeSelector == null) {
        logger.error("未知的 InkType -> beanName[{}], orderHandler.class[{}]", beanName, orderHandler.getClass());
      } else {
        logger.info("InkHandlerHolder register handler -> @[{}]", inkTypeSelector.value());
        this.container.put(inkTypeSelector.value(), orderHandler);
      }
    });
    logger.info("InkHandlerHolder register handler -> END");
  }

  public Optional<AbstractInkHandler> select(InkType inkType) {
    return Optional.ofNullable(container.get(inkType));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}

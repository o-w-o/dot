package ink.o.w.o.resource.core.symbols.service.handler;

import ink.o.w.o.resource.core.symbols.domain.SymbolsType;
import ink.o.w.o.resource.core.symbols.service.handler.ext.AbstractSymbolsHandler;
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
public class SymbolsHandlerHolder implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  private Map<SymbolsType.SymbolsTypeEnum, AbstractSymbolsHandler> container = new HashMap<>();

  @PostConstruct
  private void init() {
    logger.info("SymbolsHandlerHolder [REG] -> START");
    Map<String, AbstractSymbolsHandler> symbolsHandlers = applicationContext.getBeansOfType(AbstractSymbolsHandler.class);
    if (symbolsHandlers.isEmpty()) {
      logger.warn("SymbolsHandlerHolder [REG] -> EMPTY");
      return;
    }
    symbolsHandlers.forEach((beanName, orderHandler) -> {
      SymbolsTypeSelector symbolsTypeSelector = orderHandler.getClass().getAnnotation(SymbolsTypeSelector.class);
      if (symbolsTypeSelector == null) {
        logger.error("未知的 SymbolsType -> beanName[{}], orderHandler.class[{}]", beanName, orderHandler.getClass());
      } else {
        logger.info("SymbolsHandlerHolder [REG] -> @[{}]", symbolsTypeSelector.value());
        this.container.put(symbolsTypeSelector.value(), orderHandler);
      }
    });
    logger.info("SymbolsHandlerHolder [REG] -> END");
  }

  public Optional<AbstractSymbolsHandler> select(SymbolsType symbolsType) {
    return Optional.ofNullable(container.get(symbolsType));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}

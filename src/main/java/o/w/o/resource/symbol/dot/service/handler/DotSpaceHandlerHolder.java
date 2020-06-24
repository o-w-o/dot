package o.w.o.resource.symbol.dot.service.handler;

import o.w.o.resource.symbol.dot.repository.DotTypeRepository;
import o.w.o.resource.symbol.dot.domain.DotType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class DotSpaceHandlerHolder implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  private final Map<DotType.TypeEnum, DotSpaceHandler> container = new HashMap<>();
  @Resource
  DotTypeRepository dotTypeRepository;

  @PostConstruct
  private void init() {
    initDotHandlerHolder();
    initDotTypes();
  }

  public void initDotHandlerHolder() {
    logger.info("DotHandlerHolder [REG] -> START");
    Map<String, DotSpaceHandler> dotHandlers = applicationContext.getBeansOfType(DotSpaceHandler.class);
    if (dotHandlers.isEmpty()) {
      logger.warn("DotHandlerHolder [REG] -> EMPTY");
      return;
    }
    dotHandlers.forEach((beanName, dotHandler) -> {
      DotTypeSelector dotTypeSelector = dotHandler.getClass().getAnnotation(DotTypeSelector.class);
      if (dotTypeSelector == null) {
        logger.error("未知的 DotType -> beanName[{}], dotHandler.class[{}]", beanName, dotHandler.getClass());
      } else {
        logger.info("DotHandlerHolder [REG] -> @[{}]", dotTypeSelector.value());
        this.container.put(dotTypeSelector.value(), dotHandler);
      }
    });
    logger.info("DotHandlerHolder [REG] -> END");
  }

  public void initDotTypes() {
    dotTypeRepository.saveAll(Stream.of(DotType.TypeEnum.values()).map(DotType::new).collect(Collectors.toList()));
  }

  public Optional<DotSpaceHandler> select(DotType dotType) {
    return Optional.ofNullable(container.get(dotType.getType()));
  }

  @Override

  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}

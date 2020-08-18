package o.w.o.server.io.handler;

import lombok.extern.slf4j.Slf4j;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.system.SystemContext;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public abstract class EntityHandlerHolder<Type extends Enum<?>, Handler, Selector extends Annotation> implements ApplicationContextAware {
  private final Map<Type, Handler> container = new HashMap<>();
  private final Class<Handler> handlerClass;
  private final Class<Selector> selectorClass;
  private Handler defaultHandler;
  private ApplicationContext applicationContext;

  @Resource
  private Reflections reflections;

  @Bean
  public Reflections getResourceReflections() {
    return new Reflections(SystemContext.PKG_ENTRY + ".resource", new TypeAnnotationsScanner(), new SubTypesScanner(false));
  }

  public EntityHandlerHolder(Class<Handler> handlerClass, Class<Selector> selectorClass) {
    this.handlerClass = handlerClass;
    this.selectorClass = selectorClass;
  }

  public EntityHandlerHolder(Class<Handler> handlerClass, Class<Selector> selectorClass, Handler defaultHandler) {
    this(handlerClass, selectorClass);
    this.defaultHandler = defaultHandler;
  }

  @PostConstruct
  private void init() {
    initFieldHandlerHolder();
  }

  private void initFieldHandlerHolder() {
    logger.info("HandlerHolder [REG] -> START");
    var fieldHandlers  = reflections.getSubTypesOf(handlerClass);
    if (fieldHandlers.isEmpty()) {
      logger.warn("HandlerHolder [REG] -> EMPTY");
      return;
    }
    fieldHandlers.forEach((fieldHandler) -> {
      Selector selector = fieldHandler.getDeclaredAnnotation(selectorClass);
      if (selector == null) {
        logger.error("未知的 FieldType -> beanName[{}], fieldHandler.class[{}], [{}]", fieldHandler.getSimpleName(), fieldHandler.getClass(), fieldHandler.getClass().getAnnotations());
      } else {
        try {
          var val = (Type) selector.annotationType().getDeclaredMethod("value").invoke(selector);
          this.container.put(val, applicationContext.getBean(fieldHandler));
          logger.info("HandlerHolder [REG] -> @[{}]", val);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          logger.error("HandlerHolder [REG] Exception -> {}", e.getMessage());
        }
      }
    });
    logger.info("HandlerHolder [REG] -> END");
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public Optional<Handler> select(Type type) {
    return Optional
        .ofNullable(container.get(type))
        .or(() -> Optional.ofNullable(
            Optional
                .ofNullable(defaultHandler)
                .orElseThrow(() -> ServiceException.of(String.format("缺少类型 [%s] 的 处理器！", type))))
        );
  }
}

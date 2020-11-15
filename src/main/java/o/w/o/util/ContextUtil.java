package o.w.o.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import o.w.o.server.definition.ServiceException;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 应用上下文辅助类
 * - ApplicationContext
 * - SecurityContext
 * - RequestContext
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */
@Slf4j
public class ContextUtil {
  private static final Map<Class<?>, Object> CACHED_BEAN_MAP = new ConcurrentHashMap<>();
  private static ApplicationContext applicationContext;

  public static void initialApplicationContext(ApplicationContext ctx) {
    logger.info("initialApplicationContext -> {}", ctx.getClass().getName());
    applicationContext = ctx;
  }

  public static <T extends AbstractContext> void attachApplicationContextToInstance(T instance) {
    instance.setApplicationContext(applicationContext);
  }

  public static Optional<Object> fetchBean(String name) {
    return Optional.of(applicationContext.getBean(name));
  }

  public static <T> Optional<T> fetchBean(Class<T> clazz) {
    return Optional.of(applicationContext.getBean(clazz));
  }

  public static <T> Optional<T> fetchBean(String name, Class<T> clazz) {
    return Optional.of(applicationContext.getBean(name, clazz));
  }


  public static <T> T getBean(Class<T> clazz) {
    if (CACHED_BEAN_MAP.containsKey(clazz)) {
      return (T) CACHED_BEAN_MAP.get(clazz);
    }

    var optBean = ContextUtil.fetchBean(clazz);

    if (optBean.isPresent()) {
      CACHED_BEAN_MAP.put(clazz, optBean.get());
      return optBean.get();
    }

    logger.error("ContextUtil: Bean [{}}] not found on SystemContext !]", clazz.getSimpleName());
    throw ServiceException.of("系统运行时异常");
  }

  @Data
  @NoArgsConstructor
  public static abstract class AbstractContext {
    protected ApplicationContext applicationContext;
    protected Boolean resourceInitialStatus = false;
    protected Boolean contextInitialStatus = false;

    protected Boolean isContextInitCompletely() {
      return this.applicationContext != null;
    }

    public <T> T getBean(Class<T> beanClazz) {
      return this.applicationContext.getBean(beanClazz);
    }
  }
}

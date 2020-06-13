package o.w.o.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;


/**
 * 应用上下文辅助类
 * - ApplicationContext
 * - SecurityContext
 * - RequestContext
 * - APIContext
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */
@Slf4j
public class ContextHelper {
  private static ApplicationContext applicationContext;

  public static void setApplicationContext(ApplicationContext ctx) {
    logger.info("setApplicationContext -> {}", ctx.getClass().getName());
    applicationContext = ctx;
  }

  public static <T extends AbstractContext> void attachApplicationContextToInstance(T instance) {
    instance.setApplicationContext(applicationContext);
  }

  public static <T> T getRepository(Class<T> beanClazz) {
    return applicationContext.getBean(beanClazz);
  }

  public static <T> T getBean(Class<T> beanClazz) {
    return applicationContext.getBean(beanClazz);
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

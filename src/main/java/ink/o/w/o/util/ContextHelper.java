package ink.o.w.o.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
public class ContextHelper {
  private static ApplicationContext applicationContext;

  public static void setApplicationContext(ApplicationContext ctx) {
    logger.info("setApplicationContext -> {}", ctx.getClass().getName());
    applicationContext = ctx;
  }

  public static <T extends AbstractContext> void attachApplicationContext(T instance) {
    instance.setApplicationContext(applicationContext);
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

  @Slf4j
  @Component
  @Aspect
  public static class ContextAop {
    /**
     * 定义切入点，切入点为其中的所有函数
     * 通过 @Pointcut 注解声明频繁使用的切点表达式
     */
    @Pointcut("execution(public * ink.o.w.o.websocket.WebSocketSession.sendMessage()))")
    public void ctxAspect() {
    }

    @Around("ctxAspect()")
    public Object attachApplicationContext(ProceedingJoinPoint point) throws Throwable {
      logger.info(("拦截到了" + point.getSignature().getName() + "方法..."));
      logger.info(("拦截到了" + point.getThis() + "方法..."));

      return point.proceed();
    }
  }
}

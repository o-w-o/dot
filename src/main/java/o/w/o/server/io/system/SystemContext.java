package o.w.o.server.io.system;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SystemContext implements ApplicationContextAware {
  public static final String PKG_ENTRY = "o.w.o";

  private static ApplicationContext applicationContext;

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (SystemContext.applicationContext == null) {
      SystemContext.applicationContext = applicationContext;
    }
  }

  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }

  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }

  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }

}
    

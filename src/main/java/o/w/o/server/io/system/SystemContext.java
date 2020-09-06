package o.w.o.server.io.system;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class SystemContext implements ApplicationContextAware {
  public static final String PKG_ENTRY = "o.w.o";
  public static final String PKG_SEPARATOR = ".";
  public static final String PROJECT_PATH = System.getProperty("user.dir");
  public static final String PROJECT_GENERATE_CODE_PATH = StringUtils.joinWith(
      String.valueOf(File.separatorChar),
      PROJECT_PATH,
      "target",
      "generated-sources",
      "java"
  );
  public static final String PROJECT_SOURCE_CODE_PATH = StringUtils.joinWith(
      String.valueOf(File.separatorChar),
      PROJECT_PATH,
      "src",
      "main",
      "java"
  );

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
    

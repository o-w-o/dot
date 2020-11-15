package o.w.o.server.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * ContextHelper
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/14
 */
@Component
public class ContextHelper implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  public Optional<ApplicationContext> fetchApplicationContext() {
    return Optional.ofNullable(applicationContext);
  }

  public Optional<Object> fetchBean(String name) {
    return Optional.of(applicationContext.getBean(name));
  }

  public <T> Optional<T> fetchBean(Class<T> clazz) {
    return Optional.of(applicationContext.getBean(clazz));
  }

  public <T> Optional<T> fetchBean(String name, Class<T> clazz) {
    return Optional.of(applicationContext.getBean(name, clazz));
  }


  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
    

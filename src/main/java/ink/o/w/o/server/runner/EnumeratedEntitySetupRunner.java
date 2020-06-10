package ink.o.w.o.server.runner;

import ink.o.w.o.server.config.OrderConfiguration;
import ink.o.w.o.server.io.db.annotation.EntityEnumerated;
import ink.o.w.o.server.io.system.SystemContext;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Configuration
@Order(OrderConfiguration.DATASOURCE_INIT)
public class EnumeratedEntitySetupRunner implements ApplicationRunner {
  private void collectAndInitEnumeratedEntity() {
    logger.info("EnumeratedEntitySetupRunner:run 收集并注册 [EntityEnumerated] 类");
    Reflections reflections = new Reflections("ink.o.w.o.resource");
    Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(EntityEnumerated.class);

    classSet.forEach(v -> {
      logger.info("Reflections(ink.o.w.o.resource) clazz -> [{}]", v.getSimpleName());
      Optional.ofNullable(v.getAnnotation(EntityEnumerated.class)).ifPresent(a -> {
        var entityClass = a.entityClass();
        var repositoryClass = a.repositoryClass();

        logger.info("entityClass [{}], repositoryClass [{}]", entityClass, repositoryClass);
        Optional.ofNullable(v.getEnumConstants()).ifPresent(i -> {
          for (var o : i) {
            try {
              try {
                var e = entityClass.getDeclaredConstructor(o.getClass()).newInstance(o);
                SystemContext.getBean(repositoryClass).save(e);
              } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
              }
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
            }
          }
        });
      });

    });
  }

  @Override
  public void run(ApplicationArguments args) {
    logger.info("EnumeratedEntitySetupRunner: [START]");

    collectAndInitEnumeratedEntity();

    logger.info("EnumeratedEntitySetupRunner: [END]");
  }
}

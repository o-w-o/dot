package o.w.o.server.runner;

import lombok.extern.slf4j.Slf4j;
import o.w.o.server.definition.EnumEntityEnumerated;
import o.w.o.server.helper.ContextHelper;
import o.w.o.server.util.ReflectionUtil;
import org.reflections.Reflections;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;

/**
 * 在应用启动时，将 {@link EnumEntityEnumerated} 标记的枚举对象持久化。
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
@Slf4j
@Component
public class EnumEntityEnumeratedInitialRunner implements ApplicationRunner {
  @Resource
  private ContextHelper contextHelper;

  private void collectAndInitEnumeratedEntity() {
    logger.info("EnumeratedEntitySetupRunner: [RUN] 收集并注册 [EntityEnumerated] 类");
    Reflections reflections = ReflectionUtil.generateResourceReflection();
    Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(EnumEntityEnumerated.class);

    classSet.forEach(v -> {
      logger.info("EnumeratedEntitySetupRunner: [RUN] clazz -> [{}]", v.getSimpleName());
      Optional.ofNullable(v.getAnnotation(EnumEntityEnumerated.class)).ifPresent(a -> {
        var entityClass = a.entityClass();
        var repositoryClass = a.repositoryClass();

        logger.info("EnumeratedEntitySetupRunner: [RUN] entityClass [{}], repositoryClass [{}]", entityClass, repositoryClass);
        Optional.ofNullable(v.getEnumConstants()).ifPresent(i -> {
          for (var o : i) {
            try {
              try {
                contextHelper
                    .fetchBean(repositoryClass).orElseThrow()
                    .save(
                        entityClass
                            .getDeclaredConstructor(o.getClass())
                            .newInstance(o)
                    );
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

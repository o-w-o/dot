package ink.o.w.o.server.runner;

import ink.o.w.o.server.io.api.APIContext;
import ink.o.w.o.server.io.api.APISchemata;
import ink.o.w.o.server.io.api.annotation.APIResource;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@Order(9)
public class ApiSchemaSetupRunner implements ApplicationRunner {


  private void collectAndRegisterApiSchema() {
    logger.info("ApiSchemaSetupRunner:run 收集并注册 [ApiSchema] 类");
    Reflections reflections = new Reflections("ink.o.w.o.api", new TypeAnnotationsScanner(), new SubTypesScanner(false));
    Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(APIResource.class);

    classSet.forEach(v -> {
      logger.info("Reflections(ink.o.w.o.api) clazz -> [{}], registerApiSchema -> [{}]", v.getSimpleName(), v.getAnnotation(APIResource.class).path());
      APIContext.attachSchemaToAPIContext(v, APISchemata.of(v));
    });
    logger.info("ApiSchemaSetupRunner:run 收集并注册 [ApiSchema] 类，END");
  }

  @Override
  public void run(ApplicationArguments args) {
    logger.info("ApiSchemaSetupRunner: [START]");

    collectAndRegisterApiSchema();

    logger.info("ApiSchemaSetupRunner: [END]");
  }
}

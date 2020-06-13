package o.w.o.server.runner;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.system.SystemContext;

import java.util.Set;

@Slf4j
@Component
@Order(9)
public class ApiSchemaSetupRunner implements ApplicationRunner {


  private void collectAndRegisterApiSchema() {
    logger.info("ApiSchemaSetupRunner: [RUN] 收集并注册 [ApiSchema] 类");
    Reflections reflections = new Reflections(SystemContext.PKG_ENTRY + ".api", new TypeAnnotationsScanner(), new SubTypesScanner(false));
    Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(APIResource.class);

    classSet.forEach(v -> {
      logger.debug("ApiSchemaSetupRunner: [RUN] clazz -> [{}], registerApiSchema -> [{}]", v.getSimpleName(), v.getAnnotation(APIResource.class).path());
      APIContext.attachSchemaToAPIContext(v, APISchemata.of(v));
    });
    logger.info("ApiSchemaSetupRunner: [RUN] 收集并注册 [ApiSchema] 类，END");
  }

  @Override
  public void run(ApplicationArguments args) {
    logger.info("ApiSchemaSetupRunner: [START]");

    collectAndRegisterApiSchema();

    logger.info("ApiSchemaSetupRunner: [END]");
  }
}

package ink.o.w.o.server.runner;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
@Component
public class JsonTypesSetupRunner implements ApplicationRunner {
  @Resource
  private ObjectMapper objectMapper;

  private void collectAndRegisterJsonTypes() {
    logger.info("JsonTypesSetupRunner:run 收集并注册 [JsonTypeName] 类");
    Reflections reflections = new Reflections("ink.o.w.o.resource");
    Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(JsonTypeName.class);
    classSet.forEach(v -> {
      logger.info("Reflections(ink.o.w.o.resource) clazz -> [{}], registerSubtypes -> [{}]", v.getSimpleName(), v.getAnnotation(JsonTypeName.class).value());
      objectMapper.registerSubtypes(new NamedType(v, v.getAnnotation(JsonTypeName.class).value()));
    });
    logger.info("JsonTypesSetupRunner:run 收集并注册 [JsonTypeName] 类，END");
  }

  @Override
  public void run(ApplicationArguments args) {
    logger.info("JsonTypesSetupRunner: [START]");
    
    collectAndRegisterJsonTypes();

    logger.info("JsonTypesSetupRunner: [END]");
  }
}

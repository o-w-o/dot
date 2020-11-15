package o.w.o.server.runner;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import lombok.extern.slf4j.Slf4j;
import o.w.o.server.util.ReflectionUtil;
import org.reflections.Reflections;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 收集 {@link JsonTypeName} 标注的类，将其注入到 Jackson 中，用于抽象类子类的反序列化
 * 
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
@Slf4j
@Component
public class JsonTypesSetupRunner implements ApplicationRunner {
  @Resource
  private ObjectMapper objectMapper;

  @Override
  public void run(ApplicationArguments args) {
    logger.info("JsonTypesSetupRunner: [START]");

    logger.info("JsonTypesSetupRunner: [RUN] 收集并注册 [JsonTypeName] 类");
    Reflections reflections = ReflectionUtil.generateResourceReflection();
    Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(JsonTypeName.class);
    classSet.forEach(v -> {
      logger.info("JsonTypesSetupRunner: [RUN] Class[{}]@[{}]", v.getSimpleName(), v.getAnnotation(JsonTypeName.class).value());
      objectMapper.registerSubtypes(new NamedType(v, v.getAnnotation(JsonTypeName.class).value()));
    });
    logger.info("JsonTypesSetupRunner: [RUN] 收集并注册 [JsonTypeName] 类，END");

    logger.info("JsonTypesSetupRunner: [END]");
  }
}

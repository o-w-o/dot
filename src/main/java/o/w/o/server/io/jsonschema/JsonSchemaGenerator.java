package o.w.o.server.io.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 下午5:11
 */
public class JsonSchemaGenerator {
  public static JsonNode generateSchema(Class<?> clazz) {
    SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON)
        .with(new JacksonModule())
        .with(new JavaxValidationModule());

    SchemaGeneratorConfig config = configBuilder.build();
    SchemaGenerator generator = new SchemaGenerator(config);

    return generator.generateSchema(clazz);
  }

  public static String generateJsonSchema(Class<?> clazz) {
    return generateSchema(clazz).toString();
  }
}

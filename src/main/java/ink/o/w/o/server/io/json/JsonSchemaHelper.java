package ink.o.w.o.server.io.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 下午5:11
 */
public class JsonSchemaHelper {
  public static JsonNode generateSchema(Class<?> clazz) {
    SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON);
    SchemaGeneratorConfig config = configBuilder.build();
    SchemaGenerator generator = new SchemaGenerator(config);
    JsonNode jsonSchema = generator.generateSchema(clazz);

    return jsonSchema;
  }

  public static String generateJsonSchema(Class<?> clazz) {
    SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON);
    SchemaGeneratorConfig config = configBuilder.build();
    SchemaGenerator generator = new SchemaGenerator(config);
    JsonNode jsonSchema = generator.generateSchema(clazz);

    return jsonSchema.toString();
  }
}

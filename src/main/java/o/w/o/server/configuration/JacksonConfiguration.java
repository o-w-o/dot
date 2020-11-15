package o.w.o.server.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import o.w.o.server.definition.ServiceResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

/**
 * JacksonConfiguration
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
@Configuration
public class JacksonConfiguration {

  @Bean
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    return builder
        .createXmlMapper(false)
        .deserializerByType(ServiceResult.class, new JsonDeserializer<Object>() {
          @Override
          public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return p;
          }
        })
        .build()
        .registerModule(new Hibernate5Module())
        .registerModule(new JavaTimeModule())
        .registerModule(new Jdk8Module());
  }
}

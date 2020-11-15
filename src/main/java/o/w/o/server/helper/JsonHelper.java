package o.w.o.server.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import o.w.o.server.definition.ApiResult;
import o.w.o.server.definition.ServiceResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * JsonUtil
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/5 下午5:11
 */
@Component
public class JsonHelper {
  @Getter
  @Resource
  private ObjectMapper objectMapper;

  public <T> Optional<T> convertObject(Object o, Class<T> tClass) {
    return Optional.ofNullable(objectMapper.convertValue(o, tClass));
  }

  public JavaType constructParametricType(Class<?> wrapperClass, Class<?> innerClass) {
    return objectMapper.getTypeFactory().constructParametricType(wrapperClass, innerClass);
  }

  public JavaType constructParametricType(Class<?> wrapperClass, Class<?>... parameterClasses) {
    return objectMapper.getTypeFactory().constructParametricType(wrapperClass, parameterClasses);
  }

  public <T> Optional<T> readServiceResultPayload(String s, Class<T> tClass) throws JsonProcessingException {
    return Optional.ofNullable(
        objectMapper.<ServiceResult<T>>readValue(s, constructParametricType(ServiceResult.class, tClass))
            .getPayload()
    );
  }

  public <T> Optional<T> readApiResultPayload(String s, Class<T> tClass) throws JsonProcessingException {
    return Optional.ofNullable(
        objectMapper.<ApiResult<T>>readValue(s, constructParametricType(ApiResult.class, tClass))
            .getPayload()
    );
  }

  public <T> Optional<T> readServiceResultPayload(String s, Class<?>... parameterClasses) throws JsonProcessingException {
    return Optional.ofNullable(
        objectMapper.<ServiceResult<T>>readValue(s, constructParametricType(ServiceResult.class, parameterClasses))
            .getPayload()
    );
  }

  public <T> Optional<T> readServiceResultPayload(String s, JavaType javaType) throws JsonProcessingException {
    return Optional.ofNullable(
        objectMapper.<ServiceResult<T>>readValue(s, objectMapper.getTypeFactory().constructParametricType(ServiceResult.class, javaType))
            .getPayload()
    );
  }

  public String toJsonString(Object obj, ObjectMapper objectMapper) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

  public String toJsonString(Object obj) throws JsonProcessingException {
    return this.toJsonString(obj, objectMapper);
  }

  public <T> Optional<T> toJsonObject(String json, Class<T> tClass) throws JsonProcessingException {
    return Optional.ofNullable(objectMapper.readValue(json, tClass));
  }

  public Object toJsonObject(String json) throws JsonProcessingException {
    return toJsonObject(json, Object.class);
  }
}

package ink.o.w.o.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 下午5:11
 */
@Component
public class JsonHelper {
  private final ObjectMapper objectMapper;

  @Autowired
  JsonHelper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public ObjectMapper getObjectMapper() {
    return this.objectMapper;
  }

  /**
   * javaBean,list,array convert to json string
   */
  public String toJsonString(Object obj) throws JsonProcessingException {
    return this.toJsonString(obj, this.getObjectMapper());
  }

  public String toJsonString(Object obj, ObjectMapper objectMapper) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }
}

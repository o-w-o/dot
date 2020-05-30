package ink.o.w.o.server.io.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * APISchema
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */

@Slf4j
@Data
@Builder
public class APISchema {
  @JsonIgnore
  private Class<?> clazz;
  private String clazzMethod;

  private String namespace;
  private String api;

  private String description;
  private RequestMethod method;
  private JsonNode body;

  @Singular("pathVariables")
  private List<APIProperty> pathVariables;

  @Singular("cookies")
  private List<APIProperty> cookies;

  @Singular("headers")
  private List<APIProperty> headers;

  @Singular("parameters")
  private List<APIProperty> parameters;

  @Data
  public static class APIProperty {
    public static final String TYPE_DEFAULT = "string";

    private String name;
    private String type;
    private Boolean required;

    public APIProperty setType(String type) {
      if (StringUtils.equalsIgnoreCase(type, TYPE_DEFAULT)) {
        this.type = TYPE_DEFAULT;
      } else {
        this.type = type;
      }

      return this;
    }
  }
}

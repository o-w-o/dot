package ink.o.w.o.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RequestHelper {
  @Autowired
  private JSONHelper jsonHelper;

  /**
   * 将对象装换为map
   *
   * @param bean -
   * @return -
   */
  public <T> Map<String, Object> beanToMap(T bean) {
    return beanToMap(bean, true);
  }

  public <T> Map<String, Object> beanToMap(@NonNull T bean, Boolean skipNull) {
    Map<String, Object> map = Maps.newHashMap();
    BeanMap beanMap = BeanMap.create(bean);
    for (Object key : beanMap.keySet()) {
      if (skipNull) {
        if (beanMap.get(key) != null) {
          map.put(key + "", beanMap.get(key));
        }
      } else {
        map.put(key + "", beanMap.get(key));
      }
    }
    return map;
  }

  public <T> String stringifyQueryParameters(T parameters) {
    var builder = new StringBuilder();
    Map parameterMap = jsonHelper.getObjectMapper().convertValue(parameters, Map.class);

    parameterMap
        .keySet()
        .stream()
        .map(String::valueOf)
        .filter(parameterMap::containsKey)
        .forEach((key) -> builder
            .append(builder.indexOf("?") == -1 ? "?" : "&")
            .append(key).append("=")
            .append(parameterMap.get(key))
        );

    logger.debug("stringifyQueryParameters -> [{}]", builder.toString());
    return builder.toString();
  }
}

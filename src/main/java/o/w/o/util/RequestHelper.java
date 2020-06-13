package o.w.o.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Slf4j
@Component
public class RequestHelper {
  /**
   * 将对象装换为 map
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

  public <T> String stringifyQueryParameters(@NotNull T parameters) {
    var builder = new StringBuilder();
    try {
      var parameterMap = beanToMap(parameters);

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
    } catch (ClassCastException | IllegalArgumentException e) {
      e.printStackTrace();
    }

    logger.debug("stringifyQueryParameters -> [{}]", builder.toString());
    return builder.toString();
  }
}

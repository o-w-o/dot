package o.w.o.server.io.api;

import o.w.o.server.io.api.annotation.APIResourceSchema;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用 API 上下文辅助类
 * - APIContext
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */
@Slf4j
public class APIContext {
  private static final Map<Class<?>, APISchemata> API_CONTEXT = new ConcurrentHashMap<>();

  public static void attachSchemaToAPIContext(Class<?> clazz, APISchemata schema) {
    API_CONTEXT.put(clazz, schema);
  }

  public static Map<String, String> fetchAPIContext() {
    var map = new HashMap<String, String>();
    API_CONTEXT.forEach((aClass, apiSchemata) -> {
      if ("/".equals(apiSchemata.getNamespace())) {
        map.put(apiSchemata.getNamespace(), String.format("/%s", APIResourceSchema.SCHEMA));
      } else {
        map.put(apiSchemata.getNamespace(), String.format("/%s/%s", apiSchemata.getNamespace(), APIResourceSchema.SCHEMA));
      }
    });
    return map;
  }

  public static Optional<APISchemata> fetchAPIContext(Class<?> clazz) {
    return Optional.ofNullable(API_CONTEXT.get(clazz));
  }
}

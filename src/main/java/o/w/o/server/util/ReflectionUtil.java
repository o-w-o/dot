package o.w.o.server.util;

import org.reflections.Reflections;

import java.util.Objects;

/**
 * ReflectionUtil
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
public class ReflectionUtil {
  private static Reflections resourceReflections;

  public static String generateModulePackagePath(String module) {
    return ApplicationUtil.PKG_ENTRY + ApplicationUtil.PKG_SEPARATOR + module;
  }

  public static Reflections generateResourceReflection() {
    if (Objects.isNull(resourceReflections)) {
      resourceReflections = new Reflections(generateModulePackagePath(Modules.RESOURCE));
    }

    return resourceReflections;
  }

  public static class Modules {
    private static final String RESOURCE = "resource";
  }
}

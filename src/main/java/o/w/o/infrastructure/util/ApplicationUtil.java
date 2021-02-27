package o.w.o.infrastructure.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * ApplicationUtil
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
public class ApplicationUtil {
  public static final String PKG_ENTRY = "o.w.o";
  public static final String PKG_SEPARATOR = ".";
  public static final String RES_SYSTEM_PKG_ENTRY = PKG_ENTRY + PKG_SEPARATOR + "resource.system";

  public static final String PROJECT_PATH = System.getProperty("user.dir");
  public static final String PROJECT_SOURCE_CODE_PATH = StringUtils.joinWith(
      String.valueOf(File.separatorChar),
      PROJECT_PATH,
      "src",
      "main",
      "java"
  );
  public static final String PROJECT_GENERATE_CODE_PATH = StringUtils.joinWith(
      String.valueOf(File.separatorChar),
      PROJECT_PATH,
      "target",
      "generated-sources",
      "java"
  );
}

package o.w.o.domain.core.storage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Store 属性
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/8/14
 */
@Data
@Configuration
@ConfigurationProperties(prefix = MyStoreProperties.PREFIX)
@PropertySource(value = "classpath:/config/my.properties")
public class MyStoreProperties {
  public static final String PREFIX = "my.store";
  private String env;
  private String dir;

  public static class StorePropertyKey {
    public static final String ENV = "env";
  }

  public static class StoreEnv {
    public static final String LOCAL ="local";
    public static final String OSS_ALIYUN ="oss-aliyun";
  }

  public static class StoreDir {
    public static final String LOCAL = "local";
    public static final String TEMPORAL = "temporary";
    public static final String PUBLIC = "public";
    public static final String PROTECT = "protect";
    public static final String PRIVATE = "private";
    public static final String BIN = "bin";
    public static final String NONE = "none";
  }
}

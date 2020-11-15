package o.w.o.server.configuration.properties;

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
@ConfigurationProperties(prefix = "my.store")
@PropertySource(value = "classpath:/config/my.properties")
public class MyStoreProperties {
  private StoreType env = StoreType.OSS;

  public enum StoreType {
    LOCAL,
    OSS
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

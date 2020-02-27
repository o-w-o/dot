package ink.o.w.o.server.config.properties;

import ink.o.w.o.server.config.properties.constant.SystemRuntimeEnv;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 自定义的 App 属性
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/1/20 11:09
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "my")
@PropertySource(value = "classpath:/my.properties", ignoreResourceNotFound = true)
public class MyProperties {
  private String env = SystemRuntimeEnv.DEVELOPMENT;
  private String mail;
}
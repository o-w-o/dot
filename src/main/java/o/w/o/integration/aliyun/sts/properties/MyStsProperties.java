package o.w.o.integration.aliyun.sts.properties;

import lombok.Getter;
import lombok.Setter;
import o.w.o.integration.aliyun.core.constant.properties.MyAliyunAuthorizationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "my.aliyun.sts")
@PropertySource(value = "classpath:/config/my.properties")
public class MyStsProperties extends MyAliyunAuthorizationProperties {
  private String appName = "aliyun:sts";
  private String roleArn;
  private String roleName;
  private Long durationSeconds = 3600L;
}

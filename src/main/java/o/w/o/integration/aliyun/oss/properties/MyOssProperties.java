package o.w.o.integration.aliyun.oss.properties;

import lombok.Getter;
import lombok.Setter;
import o.w.o.integration.aliyun.core.constant.properties.MyAliyunAuthorizationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "my.aliyun.oss")
@PropertySource(value = "classpath:/config/my.properties")
public class MyOssProperties extends MyAliyunAuthorizationProperties {
  private String appName = "aliyun:oss";
  private String bucketName;
  private String bucketDomain;
  private String bucketDomainName;
  private String publicDir;
  private String temporaryDir;
  private String protectDir;
  private String privateDir;
  private String processPrefix;
  private String processDefaultName;
}

package o.w.o.resource.integration.aliyun.common.constant.properties;

import o.w.o.server.config.properties.MyAuthorizationProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 自定义的 Aliyun 属性
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/25
 */
public class MyAliyunProperties {

  @Setter
  @Getter
  public static class MyAliyunAuthorizationProperties extends MyAuthorizationProperties {
    private String appName = "aliyun";
    private Boolean enable = false;
    private String regionId;
    private String endpoint;
  }

  @Getter
  @Setter
  @Configuration
  @ConfigurationProperties(prefix = "my.aliyun.oss")
  @PropertySource(value = "classpath:/config/my.properties")
  public static class MyOssProperties extends MyAliyunAuthorizationProperties {
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

  @Getter
  @Setter
  @Configuration
  @ConfigurationProperties(prefix = "my.aliyun.sts")
  @PropertySource(value = "classpath:/config/my.properties")
  public static class MyStsProperties extends MyAliyunAuthorizationProperties {
    private String appName = "aliyun:sts";
    private String roleArn;
    private String roleName;
    private Long durationSeconds = 3600L;
  }
}

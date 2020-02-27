package ink.o.w.o.resource.aliyun.constant.properties;

import lombok.Data;
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

  @Data
  public static class MyAuthorizationProperties {
    private Boolean enable = false;
    private String regionId;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
  }

  @Getter
  @Setter
  @Configuration
  @ConfigurationProperties(prefix = "my.aliyun.oss")
  @PropertySource(value = "classpath:/my.properties")
  public static class MyOssProperties extends MyAuthorizationProperties {
    private String bucketName;
    private String bucketDomain;
    private String bucketDomainName;
    private String publicDir;
    private String temporaryDir;
    private String protectDir;
    private String privateDir;
  }

  @Getter
  @Setter
  @Configuration
  @ConfigurationProperties(prefix = "my.aliyun.sts")
  @PropertySource(value = "classpath:/my.properties")
  public static class MyStsProperties extends MyAuthorizationProperties {
    private String roleArn;
    private String roleName;
    private Long durationSeconds = 3600L;
  }
}

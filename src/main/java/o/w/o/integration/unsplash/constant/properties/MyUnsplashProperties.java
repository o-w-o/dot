package o.w.o.integration.unsplash.constant.properties;

import lombok.Getter;
import lombok.Setter;
import o.w.o.infrastructure.configuration.properties.MyAuthorizationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 自定义的 Unsplash 属性
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/3/9
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "my.unsplash")
@PropertySource(value = "classpath:/config/my.properties", ignoreResourceNotFound = true)
public class MyUnsplashProperties extends MyAuthorizationProperties {
  private String appName = "unsplash";
}

package ink.o.w.o.server.config.properties;

import ink.o.w.o.server.config.properties.constant.SystemRuntimeEnvEnum;
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
@ConfigurationProperties(prefix = "custom")
@PropertySource(value = "classpath:/custom.properties")
public class CustomProperties {
    public SystemRuntimeEnvEnum env = SystemRuntimeEnvEnum.DEVELOPMENT;
}

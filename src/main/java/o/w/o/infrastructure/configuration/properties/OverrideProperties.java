package o.w.o.infrastructure.configuration.properties;

import org.springframework.context.annotation.Profile;

/**
 * 通过环境变量中的配置文件覆盖的属性
 * @author symbols@dingtalk.com
 * @version  1.0
 * @date 2020/1/20 13:46
 */
@Profile("production")
public class OverrideProperties {
}

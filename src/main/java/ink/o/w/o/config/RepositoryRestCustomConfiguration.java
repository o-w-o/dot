package ink.o.w.o.config;

import ink.o.w.o.config.constant.HttpConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

/**
 * @author symbols@dingtalk.com
 */
@Configuration
public class RepositoryRestCustomConfiguration implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath(HttpConstant.API_ENTRY);
    }
}

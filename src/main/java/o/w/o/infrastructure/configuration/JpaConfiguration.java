package o.w.o.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

/**
 * JPAQueryFactoryConfig
 *
 * @author symbols@dingtalk.com
 * @date 2019/10/26
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "o.w.o.domain",
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = KeyValueRepository.class)
)
public class JpaConfiguration {
}

package o.w.o.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisConfiguration
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/3 下午2:33
 */

@Configuration
@EnableRedisRepositories(
    basePackages = "o.w.o.domain",
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = KeyValueRepository.class)
)
public class RedisConfiguration {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
    var template = new RedisTemplate<String, Object>();

    var stringRedisSerializer = new StringRedisSerializer();

    var genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

    template.setConnectionFactory(redisConnectionFactory);

    // key 采用 String 的序列化方式
    template.setKeySerializer(stringRedisSerializer);

    // hash 的 key 也采用 String 的序列化方式
    template.setHashKeySerializer(stringRedisSerializer);

    // value 序列化方式采用 jackson
    template.setValueSerializer(genericJackson2JsonRedisSerializer);

    // hash 的 value 序列化方式采用 jackson
    template.setHashValueSerializer(genericJackson2JsonRedisSerializer);

    template.setDefaultSerializer(genericJackson2JsonRedisSerializer);

    template.afterPropertiesSet();

    return template;
  }

}

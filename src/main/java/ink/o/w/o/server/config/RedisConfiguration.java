package ink.o.w.o.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/3 下午2:33
 */

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

  @Autowired
  ObjectMapper objectMapper;

  @Bean
  @SuppressWarnings("all")
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    var template = new RedisTemplate<String, Object>();

    template.setConnectionFactory(factory);

    var jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    jackson2JsonRedisSerializer
        .setObjectMapper(objectMapper);

    var stringRedisSerializer = new StringRedisSerializer();

    var genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

    // key 采用 String 的序列化方式
    template.setKeySerializer(stringRedisSerializer);

    // hash 的 key 也采用 String 的序列化方式
    template.setHashKeySerializer(stringRedisSerializer);

    // value 序列化方式采用 jackson
    template.setValueSerializer(jackson2JsonRedisSerializer);

    // hash 的 value 序列化方式采用 jackson
    template.setHashValueSerializer(jackson2JsonRedisSerializer);

    template.setDefaultSerializer(genericJackson2JsonRedisSerializer);

    template.afterPropertiesSet();

    return template;
  }

}

package o.w.o.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import o.w.o.infrastructure.definition.ServiceResult;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Optional;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

/**
 * 缓存配置
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/14
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfiguration {

  private RedisCacheConfiguration createCacheManagerX(Class<?> clazz) {
    return createCacheManagerX(clazz, Duration.ofMinutes(1L));
  }

  private RedisCacheConfiguration createCacheManagerX(Class<?> clazz, Duration duration) {

    return defaultCacheConfig()
        .entryTtl(duration)
        .disableCachingNullValues()
        .serializeValuesWith(
            RedisSerializationContext
                .SerializationPair
                .fromSerializer(
                    new Jackson2JsonRedisSerializer<>(clazz)
                )
        );
  }

  @Bean
  public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
    // 基本配置
    RedisCacheConfiguration defaultCacheConfiguration = defaultCacheConfig()
        // 设置 key 为 String
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
        // 设置 value 为自动转 Json 的 Object
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
        // 不缓存 null
        .disableCachingNullValues()
        // 缓存数据保存 1 分钟
        .entryTtl(Duration.ofMinutes(1));

    return RedisCacheManager.RedisCacheManagerBuilder
        // Redis 连接工厂
        .fromConnectionFactory(Optional.ofNullable(redisTemplate.getConnectionFactory()).orElseThrow())
        // 缓存配置
        .cacheDefaults(defaultCacheConfiguration)
        .withCacheConfiguration("srv", createCacheManagerX(ServiceResult.class))
        // 配置同步修改或删除 put/evict
        .transactionAware()
        .build();
  }
}

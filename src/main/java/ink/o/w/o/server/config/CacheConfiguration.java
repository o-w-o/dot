package ink.o.w.o.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ink.o.w.o.server.io.api.APIException;
import ink.o.w.o.server.io.service.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import javax.annotation.Resource;
import java.time.Duration;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

@Slf4j
@Configuration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration extends CachingConfigurerSupport {
  @Resource
  private ObjectMapper objectMapper;

  @Resource
  private RedisConnectionFactory factory;

  @Override
  @Bean
  public KeyGenerator keyGenerator() {
    return (o, method, params) -> {
      StringBuilder sb = new StringBuilder();

      sb.append(o.getClass().getName()).append("@");
      sb.append(method.getName()).append(":");
      for (Object param : params) {
        if (param != null) {
          sb.append(param.toString());
        }
      }

      String key = sb.toString();
      logger.debug("keyGenerator -> {}", key);

      return key;
    };
  }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    // 修复热部署时的缓存异常
    JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());

    return defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(1))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializationRedisSerializer));
  }

  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
    return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration());
  }

  @Override
  @Bean
  public CacheManager cacheManager() {
    RedisCacheConfiguration cacheConfigurationDurationOneMinutes =
        defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(1L));


    RedisCacheConfiguration cacheConfigurationDurationOneDay =
        defaultCacheConfig()
            .entryTtl(Duration.ofDays(1L));

    return RedisCacheManager
        .builder(factory)
        .cacheDefaults(redisCacheConfiguration())
        .withCacheConfiguration("cacheConfigurationDurationOneMinute", cacheConfigurationDurationOneMinutes)
        .withCacheConfiguration("cacheConfigurationDurationOneDay", cacheConfigurationDurationOneDay)
        .build();
  }

  private CacheManager createCacheManagerX(Class<?> clazz) {
    return createCacheManagerX(clazz, Duration.ofMinutes(1L));
  }

  private CacheManager createCacheManagerX(Class<?> clazz, Duration duration) {
    var jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(clazz);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

    RedisCacheConfiguration cacheConfiguration =
        defaultCacheConfig()
            .entryTtl(duration)
            .serializeValuesWith(
                RedisSerializationContext
                    .SerializationPair
                    .fromSerializer(
                        jackson2JsonRedisSerializer
                    )
            );
    return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
  }

  @Bean
  public CacheManager cacheManagerForServiceResult() {
    return createCacheManagerX(ServiceResult.class);
  }

  @Bean
  public CacheManager cacheManagerForHttpResponseData() {
    return createCacheManagerX(APIException.class);
  }
}

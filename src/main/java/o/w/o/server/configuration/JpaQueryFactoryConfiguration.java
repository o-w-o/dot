package o.w.o.server.configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * JPAQueryFactoryConfig
 *
 * @author symbols@dingtalk.com
 * @date 2019/10/26
 */
@Configuration
public class JpaQueryFactoryConfiguration {

  /**
   * 让 Spring 管理 JPAQueryFactory
   *
   * @author symbols@dingtalk.com
   * @date 2019/10/26
   * @since 1.0
   */
  @Bean
  @Autowired
  public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }
}

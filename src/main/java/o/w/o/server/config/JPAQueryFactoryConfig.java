package o.w.o.server.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

/**
 * JPAQueryFactoryConfig
 *
 * @author symbols@dingtalk.com
 * @date 2019/10/26
 */
public class JPAQueryFactoryConfig {

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

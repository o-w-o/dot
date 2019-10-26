package ink.o.w.o.server.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

public class JPAQueryFactoryConfig {

    /**
     * 让 Spring 管理 JPAQueryFactory
     * @author symbols@dingtalk.com
     * @since 1.0
     * @date 2019/10/26 15:53
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}

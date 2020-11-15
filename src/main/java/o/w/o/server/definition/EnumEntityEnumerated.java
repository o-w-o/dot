package o.w.o.server.definition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 EnumEntity<T> 的 枚举类，用于预设的自动注入数据库。
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumEntityEnumerated {
  Class<? extends EnumEntity> entityClass();
  Class<? extends JpaRepository> repositoryClass();
}

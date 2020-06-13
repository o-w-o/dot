package o.w.o.server.io.db.annotation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityEnumerated {
  Class<? extends Enum> enumClass();
  Class<?> entityClass();
  Class<? extends JpaRepository> repositoryClass();
}

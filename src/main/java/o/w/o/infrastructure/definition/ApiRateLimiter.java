package o.w.o.infrastructure.definition;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口调用频次限制器注解
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiRateLimiter {

  int limits() default 60;

  long duration() default 1;

  TimeUnit unit() default TimeUnit.MINUTES;
}
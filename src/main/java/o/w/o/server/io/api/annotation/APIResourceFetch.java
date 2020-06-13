package o.w.o.server.io.api.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * APIResourceOp
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.GET)
public @interface APIResourceFetch {
  @AliasFor(
      annotation = RequestMapping.class
  )
  String name() default "";

  @AliasFor(
      annotation = RequestMapping.class
  )
  String[] path() default {"/"};

  @AliasFor(
      annotation = RequestMapping.class
  )
  String[] params() default {};

  @AliasFor(
      annotation = RequestMapping.class
  )
  String[] headers() default {};

  @AliasFor(
      annotation = RequestMapping.class
  )
  String[] consumes() default {};

  @AliasFor(
      annotation = RequestMapping.class
  )
  String[] produces() default {};
}

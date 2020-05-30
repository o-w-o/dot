package ink.o.w.o.server.io.api.annotation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * APIResource 注解
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface APIResource {
  String[] path() default {"/"};
}

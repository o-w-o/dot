package o.w.o.server.io.api.annotation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * APIResource 注解
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/29
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(
    method = RequestMethod.GET,
    path = "schema",
    name = "schema"
)
@PreAuthorize("hasRole('ROLE_APISCHEMA') or hasRole('ROLE_USER')")
public @interface APIResourceSchema {
  public static final String SCHEMA = "schema";
}

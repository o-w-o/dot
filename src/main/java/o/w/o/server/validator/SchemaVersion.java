package o.w.o.server.validator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * SchemaVersion
 *
 * @author symbols@dingtalk.com
 * @date 2020/6/3
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SchemaVersionValidator.class)
public @interface SchemaVersion {
  Class<?>[] groups() default {};

  Class<?>[] payload() default {};

  String regexp() default "^v([1-9]\\d|[1-9])(\\.([1-9]\\d|\\d)){2}$";

  String message() default "版本号格式非法！";
}

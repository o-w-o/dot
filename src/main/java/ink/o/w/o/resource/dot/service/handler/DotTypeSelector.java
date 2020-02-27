package ink.o.w.o.resource.dot.service.handler;

import ink.o.w.o.resource.dot.constant.DotType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DotTypeSelector {

  DotType value();

}
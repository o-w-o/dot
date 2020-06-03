package ink.o.w.o.resource.core.dot.service.handler;

import ink.o.w.o.resource.core.dot.domain.DotType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface
DotTypeSelector {

  DotType.TypeEnum value();

}
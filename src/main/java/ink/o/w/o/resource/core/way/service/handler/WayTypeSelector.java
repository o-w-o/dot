package ink.o.w.o.resource.core.way.service.handler;

import ink.o.w.o.resource.core.way.domain.WayType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WayTypeSelector {

  WayType.WayTypeEnum value();

}
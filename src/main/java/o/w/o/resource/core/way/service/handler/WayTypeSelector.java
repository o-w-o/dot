package o.w.o.resource.core.way.service.handler;

import o.w.o.resource.core.way.domain.WayType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WayTypeSelector {

  WayType.TypeEnum value();

}
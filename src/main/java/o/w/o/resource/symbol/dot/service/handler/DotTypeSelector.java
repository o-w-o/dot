package o.w.o.resource.symbol.dot.service.handler;

import o.w.o.resource.symbol.dot.domain.DotType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface
DotTypeSelector {

  DotType.TypeEnum value();

}
package ink.o.w.o.resource.ink.service.handler;

import ink.o.w.o.resource.ink.constant.InkType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InkTypeSelector {

  InkType value();

}
package o.w.o.resource.symbols.field.service.handler;

import o.w.o.resource.symbols.field.domain.FieldType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldTypeSelector {

  FieldType.TypeEnum value();

}
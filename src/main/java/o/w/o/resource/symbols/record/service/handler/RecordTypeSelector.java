package o.w.o.resource.symbols.record.service.handler;

import o.w.o.resource.symbols.record.domain.RecordType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RecordTypeSelector {

  RecordType.TypeEnum value();

}
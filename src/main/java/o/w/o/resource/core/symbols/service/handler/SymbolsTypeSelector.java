package o.w.o.resource.core.symbols.service.handler;

import o.w.o.resource.core.symbols.domain.SymbolsType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SymbolsTypeSelector {

  SymbolsType.SymbolsTypeEnum value();

}
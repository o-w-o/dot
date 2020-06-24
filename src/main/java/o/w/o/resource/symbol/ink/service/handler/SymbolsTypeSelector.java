package o.w.o.resource.symbol.ink.service.handler;

import o.w.o.resource.symbol.ink.domain.SymbolsType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SymbolsTypeSelector {

  SymbolsType.SymbolsTypeEnum value();

}
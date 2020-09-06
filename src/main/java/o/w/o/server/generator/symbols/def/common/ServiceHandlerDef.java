package o.w.o.server.generator.symbols.def.common;

import o.w.o.server.generator.symbols.SymbolsClassName;
import o.w.o.server.generator.symbols.SymbolsMaterial;
import o.w.o.server.generator.symbols.SymbolsModule.Module;
import o.w.o.server.generator.symbols.SymbolsPackage;

import javax.lang.model.element.Modifier;

public class ServiceHandlerDef {
  public static SymbolsMaterial generateServiceDto(Module module) {
    return SymbolsMaterial
        .builder()
        .precious(true)
        .packageName(SymbolsPackage.SERVICE_DTO.generatePackageName(module))
        .className(SymbolsClassName.SERVICE_DTO.generateClassName(module))
        .modifier(Modifier.PUBLIC)
        .build();
  }
}


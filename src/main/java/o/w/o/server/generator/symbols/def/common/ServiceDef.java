package o.w.o.server.generator.symbols.def.common;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import lombok.Data;
import o.w.o.server.generator.symbols.SymbolsClassName;
import o.w.o.server.generator.symbols.SymbolsMaterial;
import o.w.o.server.generator.symbols.SymbolsModule.Module;
import o.w.o.server.generator.symbols.SymbolsPackage;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;

@Data
public class ServiceDef {
  public static SymbolsMaterial generateService(Module module) {
    return SymbolsMaterial
        .builder()
        .precious(true)
        .type(SymbolsMaterial.MaterialType.INTERFACE)
        .packageName(SymbolsPackage.SERVICE.generatePackageName(module))
        .className(SymbolsClassName.SERVICE.generateClassName(module))
        .modifier(Modifier.PUBLIC)
        .build();
  }

  public static SymbolsMaterial generateServiceImpl(Module module) {
    return SymbolsMaterial
        .builder()
        .precious(true)
        .packageName(SymbolsPackage.SERVICE_IMPL.generatePackageName(module))
        .className(SymbolsClassName.SERVICE_IMPL.generateClassName(module))
        .modifier(Modifier.PUBLIC)
        .annotation(AnnotationSpec.builder(Service.class).build())
        .superInterface(ClassName.get(SymbolsPackage.SERVICE.generatePackageName(module), SymbolsClassName.SERVICE.generateClassName(module)))
        .build();
  }
}

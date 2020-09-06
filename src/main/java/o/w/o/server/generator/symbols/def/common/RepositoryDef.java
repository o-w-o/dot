package o.w.o.server.generator.symbols.def.common;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import o.w.o.server.generator.symbols.SymbolsClassName;
import o.w.o.server.generator.symbols.SymbolsMaterial;
import o.w.o.server.generator.symbols.SymbolsModule.Module;
import o.w.o.server.generator.symbols.SymbolsPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.Modifier;

@Slf4j
@Data
public class RepositoryDef {

  public static SymbolsMaterial generateEntityRepository(Module module) {
    var domainPackageName = SymbolsPackage.REPOSITORY.generatePackageName(module);

    return SymbolsMaterial
        .builder()
        .type(SymbolsMaterial.MaterialType.INTERFACE)
        .packageName(domainPackageName)
        .className(SymbolsClassName.REPOSITORY.generateClassName(module))
        .modifier(Modifier.PUBLIC)
        .annotation(AnnotationSpec.builder(Repository.class).build())
        .superInterface(
            ParameterizedTypeName.get(
                ClassName.get(JpaRepository.class),
                ClassName.get(
                    SymbolsPackage.DOMAIN.generatePackageName(module), module.getClassName()
                ),
                ClassName.get(
                    String.class
                )
            )
        )
        .superInterface(
            ParameterizedTypeName.get(
                ClassName.get(QuerydslPredicateExecutor.class),
                ClassName.get(
                    SymbolsPackage.DOMAIN.generatePackageName(module), module.getClassName()
                )
            )
        )
        .build();
  }

  public static SymbolsMaterial generateEntityTypeRepository(Module module) {
    var domainPackageName = SymbolsPackage.REPOSITORY.generatePackageName(module);

    return SymbolsMaterial
        .builder()
        .type(SymbolsMaterial.MaterialType.INTERFACE)
        .packageName(domainPackageName)
        .className(SymbolsClassName.REPOSITORY_TYPE.generateClassName(module))
        .modifier(Modifier.PUBLIC)
        .annotation(AnnotationSpec.builder(Repository.class).build())
        .superInterface(
            ParameterizedTypeName.get(
                ClassName.get(JpaRepository.class),
                ClassName.get(
                    SymbolsPackage.DOMAIN.generatePackageName(module), SymbolsClassName.DOMAIN_TYPE.generateClassName(module)
                ),
                ClassName.get(
                    String.class
                )
            )
        )
        .superInterface(
            ParameterizedTypeName.get(
                ClassName.get(QuerydslPredicateExecutor.class),
                ClassName.get(
                    SymbolsPackage.DOMAIN.generatePackageName(module), SymbolsClassName.DOMAIN_TYPE.generateClassName(module)
                )
            )
        )
        .build();
  }

  public static SymbolsMaterial generateEntitySpaceRepository(Module module) {
    var domainPackageName = SymbolsPackage.REPOSITORY.generatePackageName(module);

    return SymbolsMaterial
        .builder()
        .type(SymbolsMaterial.MaterialType.INTERFACE)
        .packageName(domainPackageName)
        .className(SymbolsClassName.REPOSITORY_SPACE.generateClassName(module))
        .modifier(Modifier.PUBLIC)
        .annotation(AnnotationSpec.builder(Repository.class).build())
        .superInterface(
            ParameterizedTypeName.get(
                ClassName.get(JpaRepository.class),
                ClassName.get(
                    SymbolsPackage.DOMAIN.generatePackageName(module), SymbolsClassName.DOMAIN_SPACE.generateClassName(module)
                ),
                ClassName.get(
                    String.class
                )
            )
        )
        .superInterface(
            ParameterizedTypeName.get(
                ClassName.get(QuerydslPredicateExecutor.class),
                ClassName.get(
                    SymbolsPackage.DOMAIN.generatePackageName(module), module.getClassName()
                )
            )
        )
        .build();
  }

}

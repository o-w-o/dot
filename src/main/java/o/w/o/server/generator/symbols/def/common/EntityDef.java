package o.w.o.server.generator.symbols.def.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.squareup.javapoet.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import o.w.o.server.generator.symbols.SymbolsClassName;
import o.w.o.server.generator.symbols.SymbolsDatabaseNamingTpl;
import o.w.o.server.generator.symbols.SymbolsMaterial;
import o.w.o.server.generator.symbols.SymbolsModule.Module;
import o.w.o.server.generator.symbols.SymbolsModule.SubModule;
import o.w.o.server.generator.symbols.SymbolsPackage;
import o.w.o.server.io.db.EntitySpace;
import o.w.o.server.io.db.EntitySpaceWithPayload;
import o.w.o.server.io.db.EntityWithSpace;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import o.w.o.server.io.json.annotation.JsonTypeTargetType;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class EntityDef {

  public static SymbolsMaterial generateEntity(Module module) {
    var domainPackageName = SymbolsPackage.DOMAIN.generatePackageName(module);
    var domainClassName = module.getClassName();

    return SymbolsMaterial
        .builder()
        .packageName(domainPackageName)
        .className(domainClassName)
        .modifier(Modifier.PUBLIC)
        .annotations(
            List.of(
                AnnotationSpec.builder(EqualsAndHashCode.class).addMember("callSuper", "true").build(),
                AnnotationSpec.builder(NoArgsConstructor.class).build(),
                AnnotationSpec.builder(Data.class).build(),
                AnnotationSpec.builder(JsonEntityProperty.class).build(),
                AnnotationSpec.builder(Entity.class).build(),
                AnnotationSpec.builder(Table.class).addMember("name", "$S", SymbolsDatabaseNamingTpl.generateTableNameByTpl(SymbolsDatabaseNamingTpl.DOMAIN, module)).build()
            )
        )
        .superClass(
            ParameterizedTypeName.get(
                ClassName.get(EntityWithSpace.class),
                ClassName.get(
                    domainPackageName, SymbolsClassName.DOMAIN_TYPE.generateClassName(module)
                ),
                ClassName.get(
                    domainPackageName, SymbolsClassName.DOMAIN_SPACE.generateClassName(module)
                )
            )
        )
        .build();
  }

  public static SymbolsMaterial generateEntityType(Module module) {
    var domainPackageName = SymbolsPackage.DOMAIN.generatePackageName(module);

    return SymbolsMaterial
        .builder()
        .packageName(domainPackageName)
        .className(SymbolsClassName.DOMAIN_TYPE.generateClassName(module))
        .modifier(Modifier.PUBLIC)
        .annotations(
            List.of(
                AnnotationSpec.builder(NoArgsConstructor.class).build(),
                AnnotationSpec.builder(Data.class).build(),
                AnnotationSpec.builder(Entity.class).build(),
                AnnotationSpec.builder(Table.class).addMember("name", "$S", SymbolsDatabaseNamingTpl.generateTableNameByTpl(SymbolsDatabaseNamingTpl.DOMAIN_TYPE, module)).build()
            )
        )
        .fields(
            List.of(
                FieldSpec
                    .builder(Integer.class, "id", Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Id.class).build())
                    .build(),
                FieldSpec
                    .builder(getDomainTypeEnumClassName(module), "type", Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Enumerated.class).addMember("value", "$L", EnumType.STRING).build())
                    .build()
            )
        )
        .method(
            MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(getDomainTypeEnumClassName(module), "type")
                .addStatement("this.$N = $N", "type", "type")
                .addStatement("this.$N = $N.getId()", "id", "type")
                .addAnnotation(
                    AnnotationSpec.builder(JsonCreator.class).addMember("mode", "$L", JsonCreator.Mode.DISABLED).build()
                )
                .build()
        )
        .method(
            MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "typeName")
                .addCode(
                    StringUtils.joinWith("\n",
                        "$T",
                        "  .of($T.values())",
                        "  .filter(typeEnum -> $T.equals(typeEnum.getTypeName(), type))",
                        "  .findFirst()",
                        "  .ifPresentOrElse(",
                        "      typeEnum -> {",
                        "        this.id = typeEnum.getId();",
                        "        this.type = typeEnum;",
                        "      },",
                        "      () -> {",
                        "        throw $T.of($T.class);",
                        "      }",
                        ");"),
                    Stream.class,
                    getDomainTypeEnumClassName(module),
                    Objects.class,
                    JsonParseEntityEnumException.class,
                    getDomainTypeEnumClassName(module)
                )
                .addAnnotation(
                    AnnotationSpec.builder(JsonCreator.class).addMember("mode", "$L", JsonCreator.Mode.DISABLED).build()
                )
                .build()
        )
        .staticClass(ClassName.get(EnumType.class), new String[]{EnumType.STRING.name()})
        .staticClass(ClassName.get(JsonCreator.Mode.class), new String[]{JsonCreator.Mode.DISABLED.name()})
        .build();
  }

  public static SymbolsMaterial generateEntityTypeEnum(Module module) {
    var domainPackageName = SymbolsPackage.DOMAIN.generatePackageName(module);
    var domainClassName = module.getClassName();

    return SymbolsMaterial
        .builder()
        .packageName(domainPackageName)
        .className(SymbolsClassName.DOMAIN_TYPE_ENUM.generateClassName(module))
        .type(SymbolsMaterial.MaterialType.ENUM)
        .modifier(Modifier.PUBLIC)
        .annotation(
            AnnotationSpec
                .builder(EntityEnumerated.class)
                .addMember("enumClass", "$L.class", SymbolsClassName.DOMAIN_TYPE_ENUM.generateClassName(module))
                .addMember("entityClass", "$L.class", domainClassName)
                .addMember("repositoryClass", "$T.class", ClassName.get(SymbolsPackage.REPOSITORY.generatePackageName(module), SymbolsClassName.REPOSITORY.generateClassName(module)))
                .build()
        )
        .method(
            MethodSpec
                .constructorBuilder()
                .addParameter(Integer.class, "id")
                .addParameter(String.class, "typeName")
                .addStatement("this.$N = $N", "id", "id")
                .addStatement("this.$N = $N", "typeName", "typeName")
                .build()
        )
        .field(
            FieldSpec
                .builder(Integer.class, "id")
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(Getter.class)
                .build()
        )
        .field(
            FieldSpec
                .builder(String.class, "typeName")
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(Getter.class)
                .build()
        )
        .enumConstants(generateEnumConstants(module))
        .build();
  }

  public static SymbolsMaterial generateEntityTypeName(Module module) {
    var domainPackageName = SymbolsPackage.DOMAIN.generatePackageName(module);

    return SymbolsMaterial
        .builder()
        .packageName(domainPackageName)
        .className(SymbolsClassName.DOMAIN_TYPE_NAME.generateClassName(module))
        .modifier(Modifier.PUBLIC)
        .fields(generateNameConstants(module))
        .build();
  }

  public static SymbolsMaterial generateEntitySpace(Module module) {
    var domainPackageName = SymbolsPackage.DOMAIN.generatePackageName(module);
    var domainClassName = module.getClassName();

    return SymbolsMaterial
        .builder()
        .modifier(Modifier.PUBLIC)
        .modifier(Modifier.ABSTRACT)
        .packageName(domainPackageName)
        .className(SymbolsClassName.DOMAIN_SPACE.generateClassName(module))
        .superClass(
            ParameterizedTypeName.get(
                ClassName.get(EntitySpace.class),
                ClassName.get(
                    domainPackageName, SymbolsClassName.DOMAIN_TYPE.generateClassName(module)
                )
            )
        )
        .annotations(
            List.of(
                AnnotationSpec.builder(NoArgsConstructor.class).build(),
                AnnotationSpec.builder(Getter.class).build(),
                AnnotationSpec.builder(Setter.class).build(),
                AnnotationSpec.builder(JsonEntityProperty.class).build(),
                AnnotationSpec.builder(JsonTypeTargetType.class).build(),
                AnnotationSpec.builder(MappedSuperclass.class).build()
            )
        )
        .build();
  }

  public static SymbolsMaterial generateEntitySpaceWithPayload(Module module) {
    var domainPackageName = SymbolsPackage.DOMAIN.generatePackageName(module);
    var domainClassName = module.getClassName();

    return SymbolsMaterial
        .builder()
        .modifier(Modifier.PUBLIC)
        .modifier(Modifier.ABSTRACT)
        .packageName(domainPackageName)
        .className(SymbolsClassName.DOMAIN_SPACE.generateClassName(module))
        .superClass(
            ParameterizedTypeName.get(
                ClassName.get(EntitySpaceWithPayload.class),
                ClassName.get(
                    domainPackageName, SymbolsClassName.DOMAIN_TYPE_ENUM.generateClassName(module)
                ),
                TypeVariableName.get("P"),
                TypeVariableName.get("PT")
            )
        )
        .annotations(
            List.of(
                AnnotationSpec.builder(NoArgsConstructor.class).build(),
                AnnotationSpec.builder(Getter.class).build(),
                AnnotationSpec.builder(Setter.class).build(),
                AnnotationSpec.builder(JsonEntityProperty.class).build(),
                AnnotationSpec.builder(JsonTypeTargetType.class).build(),
                AnnotationSpec.builder(MappedSuperclass.class).build()
            )
        )
        .typeVariable(TypeVariableName.get("P"))
        .typeVariable(TypeVariableName.get("PT"))
        .build();
  }

  public static Map<String, TypeSpec> generateEnumConstants(Module module) {
    return Stream.of(SubModule.values()).filter(subModule -> subModule.getModule() == module).collect(
        Collectors.toMap(
            k -> StringUtils.upperCase(k.getNamespace()),
            v -> TypeSpec.anonymousClassBuilder("$L, $T.$L", v.getId(), getDomainTypeNameClassName(module), v.getEnumName()).build()
        )
    );
  }

  public static List<FieldSpec> generateNameConstants(Module module) {
    return Stream
        .of(SubModule.values())
        .filter(subModule -> subModule.getModule() == module)
        .map(SubModule::getEnumName)
        .map(enumName -> FieldSpec
            .builder(String.class, enumName)
            .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
            .initializer("$S", enumName)
            .build()
        )
        .collect(
            Collectors.toList()
        );
  }

  public static ClassName getDomainTypeNameClassName(Module module) {
    return ClassName.get(SymbolsPackage.DOMAIN.generatePackageName(module), SymbolsClassName.DOMAIN_TYPE_NAME.generateClassName(module));
  }

  public static ClassName getDomainTypeEnumClassName(Module module) {
    var domainPackageName = SymbolsPackage.DOMAIN.generatePackageName(module);
    return ClassName.get(domainPackageName, SymbolsClassName.DOMAIN_TYPE_ENUM.generateClassName(module));
  }
}

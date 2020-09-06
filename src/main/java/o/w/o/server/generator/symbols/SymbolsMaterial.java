package o.w.o.server.generator.symbols;

import com.squareup.javapoet.*;
import lombok.*;

import javax.lang.model.element.Modifier;
import java.util.*;

/**
 * 参考文档：
 * - https://github.com/square/javapoet
 * - https://blog.csdn.net/io_field/article/details/89355941
 *
 * @author symbols@dingtalk.com
 * @date 2020/9/3
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymbolsMaterial {
  boolean precious = false;

  private MaterialType type = MaterialType.CLASS;
  private String packageName;
  private String className;
  private JavaFile javaFile;
  private TypeName superClass;
  @Singular("superInterface")
  private List<TypeName> superInterfaces = new ArrayList<>();
  @Singular("typeVariable")
  private List<TypeVariableName> typeVariables = new ArrayList<>();
  @Singular("enumConstant")
  private Map<String, TypeSpec> enumConstants = new HashMap<>();
  @Singular("staticClass")
  private Map<ClassName, String[]> staticClasses = new HashMap<>();
  @Singular("modifier")
  private List<Modifier> modifiers = new ArrayList<>();
  @Singular("field")
  private List<FieldSpec> fields = new ArrayList<>();
  @Singular("annotation")
  private List<AnnotationSpec> annotations = new ArrayList<>();
  @Singular("method")
  private List<MethodSpec> methods = new ArrayList<>();

  public JavaFile compile() {
    if (!Optional.ofNullable(type).isPresent()) {
      type = MaterialType.CLASS;
    }

    var classBuilder = switch (type) {
      case CLASS, ABSTRACT_CLASS -> TypeSpec.classBuilder(className);
      case ENUM -> TypeSpec.enumBuilder(className);
      case INTERFACE -> TypeSpec.interfaceBuilder(className);
      case ANNOTATION -> TypeSpec.annotationBuilder(className);
    };

    if (type == MaterialType.ENUM) {
      enumConstants.forEach(classBuilder::addEnumConstant);
    }

    if (Optional.ofNullable(superClass).isPresent()) {
      classBuilder.superclass(
          superClass
      );
    }

    if (superInterfaces.size() > 0) {
      superInterfaces.forEach(classBuilder::addSuperinterface);
    }

    if (typeVariables.size() > 0) {
      classBuilder.addTypeVariables(typeVariables);
    }

    var javaFileBuilder = JavaFile
        .builder(
            packageName,
            classBuilder
                .addModifiers(modifiers.toArray(new Modifier[0]))
                .addAnnotations(annotations)
                .addFields(fields)
                .addMethods(methods)
                .addJavadoc(
                    SymbolsGenerator.generateJavadoc(className)
                )
                .build()
        );

    javaFileBuilder.skipJavaLangImports(true);

    staticClasses.forEach(javaFileBuilder::addStaticImport);

    return javaFileBuilder.build();
  }

  public enum MaterialType {
    CLASS,
    ABSTRACT_CLASS,
    ENUM,
    INTERFACE,
    ANNOTATION,
    ;
  }
}

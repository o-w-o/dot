package o.w.o.resource.symbols.field.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.field.repository.FieldTypeRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * DotType 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 16:42
 * @since 1.0.0
 */

@NoArgsConstructor
@Data

@Entity
@Table(name = "t_sym_field_type")
public class FieldType {
  @Id
  private Integer id;

  @Enumerated(EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public FieldType(TypeEnum dot) {
    this.id = dot.getId();
    this.type = dot;
  }

  public FieldType(String type) {
    Stream
        .of(TypeEnum.values())
        .filter(typeEnum -> Objects.equals(typeEnum.typeName, type))
        .findFirst()
        .ifPresentOrElse(
            typeEnum -> {
              this.id = typeEnum.id;
              this.type = typeEnum;
            },
            () -> {
              throw JsonParseEntityEnumException.of(TypeEnum.class);
            }
        );
  }

  public static FieldType of(TypeEnum typeEnum) {
    return new FieldType(typeEnum);
  }

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = FieldType.class, repositoryClass = FieldTypeRepository.class)
  public enum TypeEnum {

    /**
     * 资源
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    RESOURCE(20, TypeName.RESOURCE),

    /**
     * IO 字段
     *
     * @date 2020/04/20 18:05
     * @since 1.0.0
     */
    SCHEMA(30, TypeName.SCHEMA),

    /**
     * 文本
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    ELEMENT(10, TypeName.TEXT);

    /**
     * 类型名称
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    @Getter
    private final String typeName;
    @Getter
    private final Integer id;

    TypeEnum(Integer id, String typeName) {
      this.typeName = typeName;
      this.id = id;
    }
  }

  public static class TypeName {
    public static final String TEXT = "TEXT";
    public static final String RESOURCE = "RESOURCE";
    public static final String SCHEMA = "SCHEMA";
  }
}

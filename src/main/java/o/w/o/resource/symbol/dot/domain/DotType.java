package o.w.o.resource.symbol.dot.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import o.w.o.resource.symbol.dot.repository.DotTypeRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
@Table(name = "t_dot_type")
public class DotType {
  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public DotType(TypeEnum dot) {
    this.id = dot.getId();
    this.type = dot;
  }

  public DotType(String type) {
    Stream
        .of(TypeEnum.values())
        .filter(typeEnum -> Objects.equals(typeEnum.typeName, type))
        .findFirst()
        .ifPresent(typeEnum -> {
          this.id = typeEnum.id;
          this.type = typeEnum;
        });

    Optional.ofNullable(this.type).orElseThrow(() -> JsonParseEntityEnumException.of(TypeEnum.class));
  }

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = DotType.class, repositoryClass = DotTypeRepository.class)
  public enum TypeEnum {

    /**
     * 资源
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    RESOURCE(20, TypeName.RESOURCE, StoreTypeName.OSS),

    /**
     * IO 字段
     *
     * @date 2020/04/20 18:05
     * @since 1.0.0
     */
    IO_FIELD_TEXT(31, TypeName.IO_FIELD_TEXT, StoreTypeName.DB),
    IO_FIELD_PRESET_OPT_MANY(32, TypeName.IO_FIELD_PRESET_OPT_MANY, StoreTypeName.DB),
    IO_FIELD_PRESET_OPT_RANGE(33, TypeName.IO_FIELD_PRESET_OPT_RANGE, StoreTypeName.DB),
    IO_FIELD_PRESET_OPT_ONE(34, TypeName.IO_FIELD_PRESET_OPT_ONE, StoreTypeName.DB),

    /**
     * 文本
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    TEXT(1, TypeName.TEXT, StoreTypeName.DB);

    /**
     * 类型名称
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    @Getter
    private final String typeName;
    @Getter
    private final String storeTypeName;
    @Getter
    private final Integer id;

    TypeEnum(Integer id, String typeName, String storeTypeName) {
      this.typeName = typeName;
      this.storeTypeName = storeTypeName;
      this.id = id;
    }
  }

  public static class TypeName {
    public static final String TEXT = "TEXT";
    public static final String RESOURCE = "RESOURCE";

    public static final String IO_FIELD_TEXT = "IO_FIELD_TEXT";
    public static final String IO_FIELD_PRESET_OPT_MANY = "IO_FIELD_PRESET_OPT_MANY";
    public static final String IO_FIELD_PRESET_OPT_RANGE = "IO_FIELD_PRESET_OPT_RANGE";
    public static final String IO_FIELD_PRESET_OPT_ONE = "IO_FIELD_PRESET_OPT_ONE";

  }

  public static class StoreTypeName {
    public static final String OSS = "OSS";
    public static final String DB = "DB";
  }

  public static class SetConverter implements AttributeConverter<Set<TypeEnum>, String> {

    @Override
    public String convertToDatabaseColumn(Set<TypeEnum> attribute) {
      return attribute.stream().map(TypeEnum::getTypeName).reduce("", (acc, next) -> "".equals(acc) ? next : next + ",");
    }

    @Override
    public Set<TypeEnum> convertToEntityAttribute(String dbData) {
      return Set.of(dbData.split(",")).stream().map(TypeEnum::valueOf).collect(Collectors.toSet());
    }
  }
}

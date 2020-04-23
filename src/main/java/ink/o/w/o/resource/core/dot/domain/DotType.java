package ink.o.w.o.resource.core.dot.domain;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

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

  public DotType(DotTypeEnum dot){
    this.id = dot.getId();
    this.type = dot;
  }

  @Enumerated(value = EnumType.STRING)
  private DotTypeEnum type;

  public enum DotTypeEnum {

    /**
     * 引用
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    REFERENCE(41, TypeName.REFERENCE, StoreTypeName.OSS),
    LINK(51, TypeName.LINK, StoreTypeName.URL),
    COMBINATION(91, TypeName.COMBINATION, StoreTypeName.DB),

    /**
     * 资源
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    RESOURCE_PICTURE(21, TypeName.RESOURCE_PICTURE, StoreTypeName.OSS),

    RESOURCE_AUDIO(22, TypeName.RESOURCE_AUDIO, StoreTypeName.OSS),
    RESOURCE_VIDEO(23, TypeName.RESOURCE_VIDEO, StoreTypeName.OSS),
    RESOURCE_BINARY(24, TypeName.RESOURCE_BINARY, StoreTypeName.OSS),
    RESOURCE_TEXT(25, TypeName.RESOURCE_TEXT, StoreTypeName.OSS),

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

    DotTypeEnum(Integer id, String typeName, String storeTypeName) {
      this.typeName = typeName;
      this.storeTypeName = storeTypeName;
      this.id = id;
    }
  }

  public static class TypeName {
    public static final String TEXT = "TEXT";
    public static final String RESOURCE_AUDIO = "RESOURCE_AUDIO";
    public static final String RESOURCE_BINARY = "RESOURCE_BINARY";
    public static final String RESOURCE_PICTURE = "RESOURCE_PICTURE";
    public static final String RESOURCE_TEXT = "RESOURCE_TEXT";
    public static final String RESOURCE_VIDEO = "RESOURCE_VIDEO";
    public static final String LINK = "LINK";
    public static final String REFERENCE = "REFERENCE";

    public static final String IO_FIELD_TEXT = "IO_FIELD_TEXT";
    public static final String IO_FIELD_PRESET_OPT_MANY = "IO_FIELD_PRESET_OPT_MANY";
    public static final String IO_FIELD_PRESET_OPT_RANGE = "IO_FIELD_PRESET_OPT_RANGE";
    public static final String IO_FIELD_PRESET_OPT_ONE = "IO_FIELD_PRESET_OPT_ONE";

    public static final String COMBINATION = "COMBINATION";
  }

  public static class StoreTypeName {
    public static final String OSS = "OSS";
    public static final String DB = "DB";
    public static final String URL = "URL";
    public static final String URI = "URI";
  }

  public static class SetConverter implements AttributeConverter<Set<DotTypeEnum>, String> {

    @Override
    public String convertToDatabaseColumn(Set<DotTypeEnum> attribute) {
      return attribute.stream().map(DotTypeEnum::getTypeName).reduce("", (acc, next) -> "".equals(acc) ? next : next + ",");
    }

    @Override
    public Set<DotTypeEnum> convertToEntityAttribute(String dbData) {
      return Set.of(dbData.split(",")).stream().map(DotTypeEnum::valueOf).collect(Collectors.toSet());
    }
  }
}

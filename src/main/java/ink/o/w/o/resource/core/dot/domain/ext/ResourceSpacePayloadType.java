package ink.o.w.o.resource.core.dot.domain.ext;

import ink.o.w.o.resource.core.dot.repository.ext.ResourceDotTypeRepository;
import ink.o.w.o.server.io.db.annotation.EntityEnumerated;
import ink.o.w.o.server.io.json.JsonParseEntityEnumException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * ResourceSpacePayloadType
 *
 * @author symbols@dingtalk.com
 * @date 2020/06/01
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_dot__resource_type")
public class ResourceSpacePayloadType {
  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  public ResourceSpacePayloadType(TypeEnum dot) {
    this.id = dot.getId();
    this.type = dot;
  }

  public ResourceSpacePayloadType(String type) {
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

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = ResourceSpacePayloadType.class, repositoryClass = ResourceDotTypeRepository.class)
  public enum TypeEnum {
    /**
     * 资源
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    RESOURCE_PICTURE(21, TypeName.RESOURCE_PICTURE),

    RESOURCE_AUDIO(22, TypeName.RESOURCE_AUDIO),
    RESOURCE_VIDEO(23, TypeName.RESOURCE_VIDEO),
    RESOURCE_BINARY(24, TypeName.RESOURCE_BINARY),
    RESOURCE_TEXT(25, TypeName.RESOURCE_TEXT),
    ;
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
    public static final String RESOURCE_AUDIO = "RESOURCE_AUDIO";
    public static final String RESOURCE_BINARY = "RESOURCE_BINARY";
    public static final String RESOURCE_PICTURE = "RESOURCE_PICTURE";
    public static final String RESOURCE_TEXT = "RESOURCE_TEXT";
    public static final String RESOURCE_VIDEO = "RESOURCE_VIDEO";
  }


}

package o.w.o.resource.symbol.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import o.w.o.resource.symbol.dot.repository.ext.ResourceDotTypeRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;
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

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
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
    PICTURE(21, TypeName.PICTURE),

    AUDIO(22, TypeName.AUDIO),
    VIDEO(23, TypeName.VIDEO),
    BINARY(24, TypeName.BINARY),
    TEXT(25, TypeName.TEXT),
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
    public static final String AUDIO = "AUDIO";
    public static final String BINARY = "BINARY";
    public static final String PICTURE = "PICTURE";
    public static final String TEXT = "TEXT";
    public static final String VIDEO = "VIDEO";
  }


}

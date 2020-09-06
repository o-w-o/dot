package o.w.o.resource.symbols.field.domain.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.field.repository.ext.ResourceSpacePayloadTypeRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
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
@Table(name = "t_sym_field__resource_type")
public class ResourceSpacePayloadType {
  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public ResourceSpacePayloadType(TypeEnum type) {
    this.id = type.getId();
    this.type = type;
  }

  public ResourceSpacePayloadType(String type) {
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
              throw JsonParseEntityEnumException.of(ResourceSpacePayloadType.TypeEnum.class);
            }
        );
  }

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = ResourceSpacePayloadType.class, repositoryClass = ResourceSpacePayloadTypeRepository.class)
  public enum TypeEnum {
    /**
     * 资源
     *
     * @date 2020/02/12 16:42
     * @since 1.0.0
     */
    PICTURE(
        21,
        TypeName.PICTURE,
        Set.of(
            "bmp",
            "gif",
            "jpg",
            "jpeg",
            "png",
            "svg",
            "swf"
        )
    ),

    AUDIO(
        22,
        TypeName.AUDIO,
        Set.of(
            "mid",
            "wav",
            "mp3",
            "wma"
        )
    ),

    VIDEO(
        23,
        TypeName.VIDEO,
        Set.of(
            "rm",
            "rmvb",
            "mpg",
            "mpeg",
            "avi",
            "mov",
            "wmv"
        )
    ),

    BINARY(
        24,
        TypeName.BINARY,
        Set.of(
            "*"
        )
    ),

    TEXT(
        25,
        TypeName.TEXT,
        Set.of(
            "puml",
            "xml",
            "json",
            "txt"
        )
    ),
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
    @Getter
    private final Set<String> suffixes;

    TypeEnum(Integer id, String typeName, Set<String> suffixes) {
      this.typeName = typeName;
      this.id = id;
      this.suffixes = suffixes;
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
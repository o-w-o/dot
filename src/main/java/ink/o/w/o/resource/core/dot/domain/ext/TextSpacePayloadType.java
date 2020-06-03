package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import ink.o.w.o.resource.core.dot.repository.ext.TextDotTypeRepository;
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
 * TextSpacePayloadType
 *
 * @author symbols@dingtalk.com
 * @date 2020/06/02
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_dot__text_type")
public class TextSpacePayloadType {
  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public TextSpacePayloadType(TypeEnum dot) {
    this.id = dot.getId();
    this.type = dot;
  }

  public TextSpacePayloadType(String type) {
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

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = TextSpacePayloadType.class, repositoryClass = TextDotTypeRepository.class)
  public enum TypeEnum {

    SUMMARY(10, TypeName.SUMMARY),
    HEADING(11, TypeName.HEADING),
    PARAGRAPH(12, TypeName.PARAGRAPH),
    REFERENCE(13, TypeName.REFERENCE),
    CODE(14, TypeName.CODE),
    EXT_ML(15, TypeName.EXT_ML),
    EMBED(16, TypeName.EMBED),
    LINK(17, TypeName.LINK),
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
    public static final String SUMMARY = "SUMMARY";
    public static final String HEADING = "HEADING";
    public static final String PARAGRAPH = "PARAGRAPH";
    public static final String REFERENCE = "REFERENCE";
    public static final String EMBED = "EMBED";
    public static final String EXT_ML = "EXT_ML";
    public static final String CODE = "CODE";
    public static final String LINK = "LINK";
  }
}

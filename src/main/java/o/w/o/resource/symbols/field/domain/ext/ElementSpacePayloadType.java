package o.w.o.resource.symbols.field.domain.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.field.repository.ext.ElementSpacePayloadTypeRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * ObjectSpacePayloadType
 *
 * @author symbols@dingtalk.com
 * @date 2020/06/02
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_sym_field__text_type")
public class ElementSpacePayloadType {
  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public ElementSpacePayloadType(TypeEnum dot) {
    this.id = dot.getId();
    this.type = dot;
  }

  public ElementSpacePayloadType(String type) {
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

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = ElementSpacePayloadType.class, repositoryClass = ElementSpacePayloadTypeRepository.class)
  public enum TypeEnum {

    SUMMARY(10, TypeName.SUMMARY),
    HEADING(11, TypeName.HEADING),
    PARAGRAPH(12, TypeName.PARAGRAPH),
    REFERENCE(13, TypeName.REFERENCE),
    CODE(14, TypeName.CODE),
    ML_MATH(15, TypeName.ML_MATH),
    ML_UML(15, TypeName.ML_UML),
    EMBED(16, TypeName.EMBED),
    LINK(17, TypeName.LINK),
    RESOURCE(2000, TypeName.RESOURCE),
    SCHEMA(3000, TypeName.SCHEMA),
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
    public static final String HEADING = "HEADING";
    public static final String PARAGRAPH = "PARAGRAPH";
    public static final String REFERENCE = "REFERENCE";
    public static final String SUMMARY = "SUMMARY";
    public static final String EMBED = "EMBED";
    public static final String LIST = "LIST";
    public static final String CODE = "CODE";
    public static final String LINK = "LINK";
    public static final String ML_MATH = "ML_MATH";
    public static final String ML_UML = "ML_UML";
    public static final String RESOURCE = "RESOURCE";
    public static final String SCHEMA = "SCHEMA";
  }
}

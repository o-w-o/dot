package o.w.o.resource.symbols.record.domain.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.record.repository.ext.ObjectSpacePayloadTypeRepository;
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
@Table(name = "t_s_record__object_type")
public class ObjectSpacePayloadType {
  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public ObjectSpacePayloadType(TypeEnum dot) {
    this.id = dot.getId();
    this.type = dot;
  }

  public ObjectSpacePayloadType(String type) {
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

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = ObjectSpacePayloadType.class, repositoryClass = ObjectSpacePayloadTypeRepository.class)
  public enum TypeEnum {

    SCHEMA(0, TypeName.SCHEMA),
    DIARY(10, TypeName.DIARY),
    NOTE(11, TypeName.NOTE),
    LETTER(12, TypeName.LETTER),
    COLLECTION(13, TypeName.COLLECTION),
    ANNOTATION(14, TypeName.ANNOTATION),
    PRACTICE(15, TypeName.PRACTICE),
    SUMMARY(16, TypeName.SUMMARY),
    SONG_CI(17, TypeName.SONG_CI),
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
    public static final String DIARY = "DIARY";
    public static final String SONG_CI = "SONG_CI";
    public static final String NOTE = "NOTE";
    public static final String LETTER = "LETTER";
    public static final String COLLECTION = "COLLECTION";
    public static final String ANNOTATION = "ANNOTATION";
    public static final String PRACTICE = "PRACTICE";
    public static final String SUMMARY = "SUMMARY";
    public static final String SCHEMA = "SCHEMA";
  }
}

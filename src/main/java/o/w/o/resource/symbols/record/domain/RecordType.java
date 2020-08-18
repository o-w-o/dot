package o.w.o.resource.symbols.record.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.record.repository.RecordTypeRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * RecordType 枚举类型
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_s_record_type")
public class RecordType {

  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public RecordType(TypeEnum typeEnum) {
    this.id = typeEnum.id;
    this.type = typeEnum;
  }

  public RecordType(String type) {
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

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = RecordType.class, repositoryClass = RecordTypeRepository.class)
  public enum TypeEnum {
    /**
     * 主观
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    OBJECT(1, TypeName.OBJECT),

    /**
     * 辩证
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    DIALECTIC(9, TypeName.DIALECTIC),

    /**
     * 客观
     *
     * @date 2020/02/12 16:41
     * @since 1.0.0
     */
    SUBJECT(2, TypeName.SUBJECT),
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
    public static final String OBJECT = "OBJECT";
    public static final String SUBJECT = "SUBJECT";
    public static final String DIALECTIC = "DIALECTIC";
  }
}

package o.w.o.resource.core.org.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import o.w.o.resource.core.org.repository.OrgTypeRepository;
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
 * OrgType
 *
 * @author symbols@dingtalk.com
 * @date 2020/6/4
 */

@NoArgsConstructor
@Data

@Entity
@Table(name = "t_org_type")
public class OrgType {

  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public OrgType(TypeEnum typeEnum) {
    this.id = typeEnum.id;
    this.type = typeEnum;
  }

  public OrgType(String type) {
    Stream
        .of(OrgType.TypeEnum.values())
        .filter(typeEnum -> Objects.equals(typeEnum.typeName, type))
        .findFirst()
        .ifPresent(typeEnum -> {
          this.id = typeEnum.id;
          this.type = typeEnum;
        });

    Optional.ofNullable(this.type).orElseThrow(() -> JsonParseEntityEnumException.of(OrgType.TypeEnum.class));

  }

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = OrgType.class, repositoryClass = OrgTypeRepository.class)
  public enum TypeEnum {
    SET(1, TypeName.SET),
    TREE(2, TypeName.TREE),
    RANK(3, TypeName.RANK),
    CLASSIFICATION(9, TypeName.CLASSIFICATION);

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
    public static final String SET = "SET";
    public static final String TREE = "TREE";
    public static final String RANK = "RANK";
    public static final String CLASSIFICATION = "CLASSIFICATION";
  }
}

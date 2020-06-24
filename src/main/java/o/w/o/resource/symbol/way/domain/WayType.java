package o.w.o.resource.symbol.way.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import o.w.o.resource.symbol.way.repository.WayTypeRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@NoArgsConstructor
@Data

@Entity
@Table(name = "t_way_type")
public class WayType {

  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private TypeEnum type;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public WayType(TypeEnum typeEnum) {
    this.id = typeEnum.getId();
    this.type = typeEnum;
  }

  public WayType(String type) {
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

  @EntityEnumerated(enumClass = TypeEnum.class, entityClass = WayType.class, repositoryClass = WayTypeRepository.class)
  public enum TypeEnum {
    SET(0, TypeName.SET),
    TREE(0, TypeName.TREE),
    NET(0, TypeName.NET);

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
    public static final String NET = "NET";
  }
}

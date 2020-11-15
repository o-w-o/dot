package o.w.o.resource.example.enumentity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.server.definition.EnumEntity;
import o.w.o.server.definition.EnumEntityEnumerated;
import o.w.o.server.definition.EnumNotExistItemException;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * EnumEntityExample
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/27
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_example_enum")
public class EnumEntityExample extends EnumEntity<EnumEntityExample.Enum> {

  @Id
  private Integer id;

  @Column(unique = true, nullable = false)
  private String name;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public EnumEntityExample(Enum v) {
    this.id = v.getId();
    this.name = v.getName();
    this.mountedEnumValue(v);
  }

  @JsonCreator
  public EnumEntityExample(String name) {
    Stream
        .of(Enum.values())
        .filter(v -> Objects.equals(v.name, name))
        .findFirst()
        .ifPresentOrElse(v -> {
          this.id = v.id;
          this.name = v.name;
          this.mountedEnumValue(v);
        }, () -> {
          if (this.throwIfItemNonexistent()) {
            throw EnumNotExistItemException.of(Enum.class);
          }

          this.id = getIntegerUndefinedMark();
          this.name = name;
        });
  }

  @PrePersist
  public void prePersist() {
    if (this.id.equals(getIntegerUndefinedMark())) {
      throw getUndefinedException();
    }
  }

  @EnumEntityEnumerated(entityClass = EnumEntityExample.class, repositoryClass = EnumEntityExampleRepository.class)
  public enum Enum {
    X(1, "x"),
    Y(2, "y");

    @Getter
    private final Integer id;
    @Getter
    private final String name;

    Enum(Integer id, String name) {
      this.id = id;
      this.name = name;
    }
  }
}

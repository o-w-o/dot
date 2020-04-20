package ink.o.w.o.resource.core.way.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data

@Entity
@Table(name = "t_way_type")
public class WayType {

  @Id
  private String id;

  @Enumerated(value = EnumType.STRING)
  private WayTypeEnum type;

  public enum WayTypeEnum {
    SET(WayTypeName.SET),
    TREE(WayTypeName.TREE),
    NET(WayTypeName.NET);

    @Getter
    private final String typeName;

    private WayTypeEnum(String typeName) {
      this.typeName = typeName;
    }
  }

  public static class WayTypeName {
    public static final String SET = "SET";
    public static final String TREE = "TREE";
    public static final String NET = "NET";
  }
}

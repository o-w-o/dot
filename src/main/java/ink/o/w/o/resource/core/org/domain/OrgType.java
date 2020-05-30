package ink.o.w.o.resource.core.org.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data

@Entity
@Table(name = "t_org_type")
public class OrgType {

  @Id
  private String id;

  @Enumerated(value = EnumType.STRING)
  private OrgTypeEnum type;

  public enum OrgTypeEnum {
    SET(OrgTypeName.SET),
    TREE(OrgTypeName.TREE),
    NET(OrgTypeName.NET);

    @Getter
    private final String typeName;

    private OrgTypeEnum(String typeName) {
      this.typeName = typeName;
    }
  }

  public static class OrgTypeName {
    public static final String SET = "SET";
    public static final String TREE = "TREE";
    public static final String NET = "NET";
    public static final String CLASSIFICATION = "CLASSIFICATIONp";
  }
}

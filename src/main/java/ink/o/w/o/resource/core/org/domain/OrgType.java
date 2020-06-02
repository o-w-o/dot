package ink.o.w.o.resource.core.org.domain;

import ink.o.w.o.resource.core.org.repository.OrgTypeRepository;
import ink.o.w.o.server.io.db.annotation.EntityEnumerated;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data

@Entity
@Table(name = "t_org_type")
public class OrgType {

  @Id
  private Integer id;

  @Enumerated(value = EnumType.STRING)
  private OrgTypeEnum type;

  public OrgType(OrgTypeEnum org) {
    this.id = org.id;
    this.type = org;
  }

  @EntityEnumerated(enumClass = OrgTypeEnum.class, entityClass = OrgType.class, repositoryClass = OrgTypeRepository.class)
  public enum OrgTypeEnum {
    SET(1, OrgTypeName.SET),
    TREE(2, OrgTypeName.TREE),
    NET(3, OrgTypeName.NET);

    @Getter
    private final String typeName;
    @Getter
    private final Integer id;

    private OrgTypeEnum(Integer id, String typeName) {
      this.typeName = typeName;
      this.id = id;
    }
  }

  public static class OrgTypeName {
    public static final String SET = "SET";
    public static final String TREE = "TREE";
    public static final String NET = "NET";
    public static final String CLASSIFICATION = "CLASSIFICATION";
  }
}

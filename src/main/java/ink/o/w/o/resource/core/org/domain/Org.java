package ink.o.w.o.resource.core.org.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import ink.o.w.o.server.io.db.EntityWithSpace;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.Valid;

@NoArgsConstructor
@Data

@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})

@Entity
@Table(name = "t_org")
public class Org extends EntityWithSpace<OrgType, OrgSpace> {
  @Id
  @GeneratedValue(generator = "org-uuid")
  @GenericGenerator(name = "org-uuid", strategy = "uuid")
  private String id;

  @ManyToOne
  private OrgType type;

  @Valid
  @Transient
  @JsonTypedSpace
  private OrgSpace space;
  private String spaceId;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Object spaceContent;
}

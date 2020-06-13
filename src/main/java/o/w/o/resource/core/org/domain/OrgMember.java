package o.w.o.resource.core.org.domain;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

/**
 * OrgSpaceMember
 *
 * @author symbols@dingtalk.com
 * @date 2020/5/31 17:00
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})

@Entity
@Table(name = "t_org_member")
public class OrgMember {
  /**
   * Id
   *
   * @date 2020/5/31
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "org_space_member-uuid")
  @GenericGenerator(name = "org_space_member-uuid", strategy = "uuid")
  private String id;

  @ManyToOne
  private Org org;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Object payload;
}

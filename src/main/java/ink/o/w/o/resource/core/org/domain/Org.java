package ink.o.w.o.resource.core.org.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@NoArgsConstructor
@Data

@Entity
@Table(name = "t_org")
public class Org {
  @Id
  @GeneratedValue(generator = "org-uuid")
  @GenericGenerator(name = "org-uuid", strategy = "uuid")
  private String id;

  @ManyToOne
  private OrgType type;
}

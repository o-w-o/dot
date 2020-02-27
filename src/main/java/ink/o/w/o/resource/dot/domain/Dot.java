package ink.o.w.o.resource.dot.domain;


import ink.o.w.o.resource.dot.constant.DotType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_dot")
public class Dot {
  @Id
  @GeneratedValue(generator = "dot-uuid")
  @GenericGenerator(name = "dot-uuid", strategy = "uuid")
  private String id;

  @Enumerated(value = EnumType.STRING)
  private DotType type;

  @Lob
  private String space;
}

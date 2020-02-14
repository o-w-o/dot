package ink.o.w.o.resource.ink.domain;


import ink.o.w.o.resource.ink.constant.InkUnitType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_ink_unit")
public class InkUnit {
  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private InkUnitType type;
}

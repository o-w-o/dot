package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import ink.o.w.o.resource.core.dot.domain.DotType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.IO_FIELD_PRESET_OPT_MANY)

@Entity
@Table(name = "t_dot__iofield_preset_opt_range")
public class IOFieldPresetOptRangeDot extends DotSpace {
  @OneToOne
  private Dot referenceDot;

  @Override
  public DotType.DotTypeEnum getType() {
    return DotType.DotTypeEnum.IO_FIELD_PRESET_OPT_MANY;
  }
}

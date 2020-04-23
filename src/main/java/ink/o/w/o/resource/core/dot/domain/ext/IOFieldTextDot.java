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

@JsonTypeName(DotType.TypeName.IO_FIELD_TEXT)

@Entity
@Table(name = "t_dot__iofield_text")
public class IOFieldTextDot extends DotSpace {
  @Override
  public DotType.DotTypeEnum getType() {
    return DotType.DotTypeEnum.IO_FIELD_TEXT;
  }

  @OneToOne
  private Dot referenceDot;
}

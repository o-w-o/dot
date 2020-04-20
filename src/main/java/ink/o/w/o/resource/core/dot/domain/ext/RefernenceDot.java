package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.REFERENCE)

@Entity
@Table(name = "t_dot__reference")
public class RefernenceDot extends DotSpace {

  @OneToOne
  private Dot referenceDot;
}

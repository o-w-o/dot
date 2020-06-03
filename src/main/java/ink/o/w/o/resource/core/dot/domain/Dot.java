package ink.o.w.o.resource.core.dot.domain;


import ink.o.w.o.resource.core.symbols.domain.Symbols;
import ink.o.w.o.server.io.db.EntityWithSpace;
import ink.o.w.o.server.io.json.annotation.JsonEntityProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

@JsonEntityProperty

@Entity
@Table(name = "t_dot")
public class Dot extends EntityWithSpace<DotType, DotSpace> {
  @ManyToOne
  private Symbols sourceSymbols;
}

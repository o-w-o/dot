package ink.o.w.o.resource.core.symbols.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "t_symbols_mark")
public class SymbolsMark {
  @Id
  private Long id;
}

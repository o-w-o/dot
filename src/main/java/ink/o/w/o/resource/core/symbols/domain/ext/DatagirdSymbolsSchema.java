package ink.o.w.o.resource.core.symbols.domain.ext;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@Data

@Entity
@Table(name = "t_symbols__datagird_schema")
public class DatagirdSymbolsSchema implements Serializable {
  private static final long serialVersionUID = 6823485920201453252L;

  @Id
  protected String id;
}

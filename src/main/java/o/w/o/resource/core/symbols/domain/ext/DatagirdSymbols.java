package o.w.o.resource.core.symbols.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import o.w.o.resource.core.symbols.domain.SymbolsSpace;
import o.w.o.resource.core.symbols.domain.SymbolsType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(SymbolsType.TypeName.DATAGIRD)

@Entity
@Table(name = "t_symbols__datagird")
public class DatagirdSymbols extends SymbolsSpace implements Serializable {
  private static final long serialVersionUID = -2821708322740399849L;
  @OneToOne(fetch = FetchType.LAZY)
  private DatagirdSymbolsSchema schema;
  @Lob
  private String data;

  @Override
  public SymbolsType.SymbolsTypeEnum getType() {
    return SymbolsType.SymbolsTypeEnum.DATAGIRD;
  }

}

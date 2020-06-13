package o.w.o.resource.core.symbols.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import o.w.o.resource.core.symbols.domain.Symbols;
import o.w.o.resource.core.symbols.domain.SymbolsSpace;
import o.w.o.resource.core.symbols.domain.SymbolsType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(SymbolsType.TypeName.DERIVANT)

@Entity
@Table(name = "t_symbols__derivant")
public class DerivantSymbols extends SymbolsSpace implements Serializable {
  private static final long serialVersionUID = 2582992954173051962L;
  @ManyToOne
  private Symbols derivativeSymbols;

  @Override
  public SymbolsType.SymbolsTypeEnum getType() {
    return SymbolsType.SymbolsTypeEnum.DERIVANT;
  }
}

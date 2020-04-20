package ink.o.w.o.resource.core.symbols.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.symbols.domain.SymbolsSpace;
import ink.o.w.o.resource.core.symbols.domain.SymbolsType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(SymbolsType.TypeName.DOCUMENT)

@Entity
@Table(name = "t_symbols__template")
public class TemplateSymbols extends SymbolsSpace implements Serializable {
  private static final long serialVersionUID = 2582992954173051962L;
  private String summary;
  private String content;

  @Override
  public SymbolsType.SymbolsTypeEnum getType() {
    return SymbolsType.SymbolsTypeEnum.DOCUMENT;
  }
}

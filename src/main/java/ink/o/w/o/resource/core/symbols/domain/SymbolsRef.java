package ink.o.w.o.resource.core.symbols.domain;

import ink.o.w.o.resource.core.symbols.constant.SymbolsRefMode;
import lombok.Data;

import javax.persistence.*;


/**
 * InkRef
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 15:41
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "t_symbols_ref")
public class SymbolsRef {
  @Id
  private Long id;

  @ManyToOne
  private Symbols symbols;

  @Enumerated(value = EnumType.STRING)
  private SymbolsRefMode mode;
}

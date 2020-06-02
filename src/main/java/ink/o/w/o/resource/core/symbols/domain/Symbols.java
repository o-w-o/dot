package ink.o.w.o.resource.core.symbols.domain;

import ink.o.w.o.server.io.db.EntityWithSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 墨水
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_symbols")
public class Symbols extends EntityWithSpace<SymbolsType, SymbolsSpace> {
  /**
   * 标题
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  protected String title;
  protected String description;

}

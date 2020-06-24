package o.w.o.resource.symbol.way.domain;

import o.w.o.server.io.db.EntityWithSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;


/**
 * Ink
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data

@Entity
@Table(name = "t_way")
public class Way extends EntityWithSpace<WayType, WaySpace> {
  @OneToMany
  private Set<WayTrace> traces;
}

package ink.o.w.o.resource.core.way.domain;

import ink.o.w.o.server.io.db.EntityIdentity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


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
@Table(name = "t_way_trace")
public class WayTrace extends EntityIdentity {
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  public Object traceContent;
}

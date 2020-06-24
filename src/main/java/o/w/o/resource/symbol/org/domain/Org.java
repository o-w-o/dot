package o.w.o.resource.symbol.org.domain;

import o.w.o.server.io.db.EntityWithSpace;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Org
 *
 * @author symbols@dingtalk.com
 * @date 2020/6/4
 */

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@Entity
@Table(name = "t_org")
public class Org extends EntityWithSpace<OrgType, OrgSpace> {
  @OneToMany
  private List<OrgMember> members;

}

package ink.o.w.o.resource.system.role.domain;

import ink.o.w.o.resource.system.role.util.RoleHelper;
import ink.o.w.o.server.io.service.ServiceException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Role
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:32
 */
@EqualsAndHashCode(callSuper = true)
@Data

@Entity
@Table(name = "t_role")
public class Role extends RepresentationModel<Role> implements Serializable {

  private static final long serialVersionUID = 1634634962611441758L;

  @Id
  private Integer id;

  @Column(unique = true, nullable = false)
  private String name;

  private boolean system;

  @PreRemove
  public void preRemove() {
    if (RoleHelper.ROLES_MAP.containsKey(this.name)) {
      throw new ServiceException("系统权限不得删除！");
    }
  }

  @PrePersist
  public void prePersist() {
    this.setSystem(RoleHelper.ROLES_MAP.containsKey(this.name));
  }
}

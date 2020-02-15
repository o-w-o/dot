package ink.o.w.o.resource.role.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Role
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:32
 */
@Entity
@Table(name = "t_role")

@Data
public class Role implements Serializable {

  private static final long serialVersionUID = 1634634962611441758L;

  @Id
  private Integer id;

  @NotNull
  private String name;
}

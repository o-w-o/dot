package o.w.o.resource.system.role.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import o.w.o.resource.system.role.repository.RoleRepository;
import o.w.o.server.io.db.annotation.EntityEnumerated;
import o.w.o.server.io.json.JsonParseEntityEnumException;
import o.w.o.server.io.service.ServiceException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Role
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2020/2/5 19:32
 */
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_sys_role")
public class Role implements Serializable {
  public static final String ROLE_SEPARATOR = "%";
  public static final String ROLE_PREFIX = "ROLE_";

  public final static Role ANONYMOUS = new Role(Enum.ANONYMOUS);
  public final static Role MASTER = new Role(Enum.MASTER);
  public final static Role USER = new Role(Enum.USER);
  public final static Role ENDPOINT = new Role(Enum.ENDPOINT);
  public final static Role RESOURCES = new Role(Enum.RESOURCES);
  public final static Role RESOURCES_USER = new Role(Enum.RESOURCES_USER);
  public final static Role RESOURCES_ROLE = new Role(Enum.RESOURCES_ROLE);
  public final static Role RESOURCES_SAMPLE = new Role(Enum.RESOURCES_SAMPLE);

  private static final long serialVersionUID = 1634634962611441758L;

  @Id
  private Integer id;

  @Column(unique = true, nullable = false)
  private String name;

  private Boolean system;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public Role(Enum typeEnum) {
    this.id = typeEnum.getId();
    this.name = typeEnum.roleName;
    this.system = true;
  }

  public Role(String roleName) {
    Stream
        .of(Enum.values())
        .filter(typeEnum -> Objects.equals(typeEnum.roleName, roleName))
        .findFirst()
        .ifPresentOrElse(typeEnum -> {
          this.id = typeEnum.id;
          this.name = typeEnum.roleName;
          this.system = true;
        }, () -> {
          throw JsonParseEntityEnumException.of(Enum.class);
        });
  }

  public static boolean isSystem(String roleName) {
    return Stream.of(Enum.values()).filter(typeEnum -> Objects.equals(typeEnum.roleName, roleName)).count() == 1;
  }

  @PreRemove
  public void preRemove() {
    if (isSystem(this.name)) {
      throw new ServiceException("系统权限不得删除！");
    }
  }

  @PrePersist
  public void prePersist() {
    if (this.system == null) {
      this.setSystem(isSystem(this.name));
    }
  }

  @EntityEnumerated(enumClass = Enum.class, entityClass = Role.class, repositoryClass = RoleRepository.class)
  public enum Enum {
    ANONYMOUS(0, "ANONYMOUS"),
    MASTER(1, "MASTER"),
    USER(9, "USER"),
    ENDPOINT(10, "ENDPOINT"),
    RESOURCES(11, "RESOURCES"),
    RESOURCES_USER(111, "RESOURCES:USER"),
    RESOURCES_ROLE(112, "RESOURCES:ROLE"),
    RESOURCES_SAMPLE(113, "RESOURCES:SAMPLE"),
    ;

    @Getter
    Integer id;
    @Getter
    String roleName;

    Enum(Integer id, String roleName) {
      this.id = id;
      this.roleName = roleName;
    }
  }
}

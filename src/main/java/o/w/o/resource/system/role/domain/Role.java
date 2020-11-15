package o.w.o.resource.system.role.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.system.role.repository.RoleRepository;
import o.w.o.server.definition.EnumEntity;
import o.w.o.server.definition.EnumEntityEnumerated;
import o.w.o.server.definition.EnumNotExistItemException;
import o.w.o.server.definition.ServiceException;

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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

@Entity
@Table(name = "t_sys_role")
public class Role extends EnumEntity<Role.Enum> implements Serializable {
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

  @Column(unique = true, nullable = false)
  private String name;

  private Boolean system;

  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public Role(Enum v) {
    this.id = v.getId();
    this.name = v.roleName;
    this.system = true;
    this.mountedEnumValue(v);
  }

  public Role(String roleName) {
    Stream
        .of(Enum.values())
        .filter(v -> Objects.equals(v.roleName, roleName))
        .findFirst()
        .ifPresentOrElse(v -> {
          this.id = v.id;
          this.name = v.roleName;
          this.system = true;
          this.mountedEnumValue(v);
        }, () -> {
          if (throwIfItemNonexistent()) {
            throw EnumNotExistItemException.of(Enum.class);
          }

          this.id = getIntegerUndefinedMark();
          this.name = roleName;
        });
  }

  @Override
  protected boolean throwIfItemNonexistent() {
    return false;
  }

  @PreRemove
  public void preRemove() {
    if (this.system) {
      throw new ServiceException("系统权限不得删除！");
    }
  }

  @PrePersist
  public void prePersist() {
    if (getIntegerUndefinedMark().equals(this.id)) {
      throw getUndefinedException();
    }
  }

  @EnumEntityEnumerated(entityClass = Role.class, repositoryClass = RoleRepository.class)
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
    private final Integer id;
    @Getter
    private final String roleName;

    Enum(Integer id, String roleName) {
      this.id = id;
      this.roleName = roleName;
    }
  }
}

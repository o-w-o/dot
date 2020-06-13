package o.w.o.resource.system.role.util;

import com.google.common.base.Joiner;
import o.w.o.resource.system.role.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RoleHelper {
  public final static Map<String, Role> ROLES_MAP = Map.of(
      Role.MASTER.getName(), Role.MASTER,
      Role.ENDPOINT.getName(), Role.ENDPOINT,
      Role.RESOURCES.getName(), Role.RESOURCES,
      Role.RESOURCES_USER.getName(), Role.RESOURCES_USER,
      Role.RESOURCES_ROLE.getName(), Role.RESOURCES_ROLE,
      Role.RESOURCES_SAMPLE.getName(), Role.RESOURCES_SAMPLE,
      Role.USER.getName(), Role.USER
  );

  public static Set<Role> toRoles(Role... roles) {
    Set<Role> roleSet = new HashSet<>();

    if (roles == null || roles.length == 0) {
      return roleSet;
    }

    Collections.addAll(roleSet, roles);
    return roleSet;
  }

  public static String toRolesString(Set<Role> roles) {
    String roleString = Joiner.on(Role.ROLE_SEPARATOR).skipNulls().join(
        roles.stream().map(Role::getName).iterator()
    );

    logger.debug("toRolesString [{}]", roleString);
    return roleString;
  }

  public static String toRolesString(Role... roles) {
    return toRolesString(Set.of(roles));
  }

  public static Set<Role> fromRolesString(String roleString) {
    String[] roleArray = roleString.split(Role.ROLE_SEPARATOR);
    logger.debug("fromRolesString:roleArray -> {} - {} - {}", roleString, roleArray.length, roleArray[0]);

    Set<Role> roleSet = Set.of(
        roleString.split(Role.ROLE_SEPARATOR)
    )
        .stream()
        .map(ROLES_MAP::get)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    logger.debug("fromRolesString:roleSet -> {}", roleSet.size());

    return roleSet;
  }

  public static List<GrantedAuthority> toAuthorities(Set<Role> roles) {
    List<GrantedAuthority> list = new ArrayList<>();

    roles.forEach((role) -> list.add(new SimpleGrantedAuthority(Role.ROLE_PREFIX + role.getName())));

    return list;
  }
}

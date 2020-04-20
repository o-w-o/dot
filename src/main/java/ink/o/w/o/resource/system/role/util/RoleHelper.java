package ink.o.w.o.resource.system.role.util;

import ink.o.w.o.resource.system.role.constant.RoleConstant;
import ink.o.w.o.resource.system.role.constant.Roles;
import ink.o.w.o.resource.system.role.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RoleHelper {
  public final static Map<String, Role> ROLES_MAP = Map.of(
      Roles.MASTER.getName(), Roles.MASTER,
      Roles.ENDPOINT.getName(), Roles.ENDPOINT,
      Roles.RESOURCES.getName(), Roles.RESOURCES,
      Roles.RESOURCES_USER.getName(), Roles.RESOURCES_USER,
      Roles.RESOURCES_ROLE.getName(), Roles.RESOURCES_ROLE,
      Roles.RESOURCES_SAMPLE.getName(), Roles.RESOURCES_SAMPLE,
      Roles.USER.getName(), Roles.USER
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
    String roleString = StringUtils.join(
        roles.stream().map(Role::getName).collect(Collectors.toSet()), RoleConstant.ROLE_SEPARATOR
    );

    logger.debug("toRolesString [{}]", roleString);
    return roleString;
  }

  public static String toRolesString(Role... roles) {
    return StringUtils.join(
        Set.of(roles).stream().map(Role::getName), RoleConstant.ROLE_SEPARATOR
    );
  }

  public static Set<Role> fromRolesString(String roleString) {
    String[] roleArray = roleString.split(RoleConstant.ROLE_SEPARATOR);
    logger.debug("fromRolesString:roleArray -> {} - {} - {}", roleString, roleArray.length, roleArray[0]);

    Set<Role> roleSet = Set.of(
        roleString.split(RoleConstant.ROLE_SEPARATOR)
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

    roles.forEach((role) -> {
      list.add(new SimpleGrantedAuthority(RoleConstant.ROLE_PREFIX + role.getName()));
    });

    return list;
  }
}

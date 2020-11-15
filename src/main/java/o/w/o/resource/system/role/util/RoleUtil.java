package o.w.o.resource.system.role.util;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.role.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RoleUtil {
  public final static Map<String, Role> ROLES_PRESETS = Map.of(
      Role.MASTER.getName(), Role.MASTER,
      Role.ENDPOINT.getName(), Role.ENDPOINT,
      Role.RESOURCES.getName(), Role.RESOURCES,
      Role.RESOURCES_USER.getName(), Role.RESOURCES_USER,
      Role.RESOURCES_ROLE.getName(), Role.RESOURCES_ROLE,
      Role.RESOURCES_SAMPLE.getName(), Role.RESOURCES_SAMPLE,
      Role.USER.getName(), Role.USER
  );

  public static Set<Role> of(Role... roles) {
    Set<Role> roleSet = new HashSet<>();

    if (roles == null || roles.length == 0) {
      return roleSet;
    }

    Collections.addAll(roleSet, roles);
    return roleSet;
  }

  public static Set<Role> from(String roleString) {
    String[] roleArray = roleString.split(Role.ROLE_SEPARATOR);
    logger.debug("from:roles -> {} - {} - {}", roleString, roleArray.length, roleArray[0]);

    Set<Role> roleSet = Set.of(
        roleString.split(Role.ROLE_SEPARATOR)
    )
        .stream()
        .map(Role::new)
        .collect(Collectors.toSet());
    logger.debug("fromRolesString:roleSet -> {}", roleSet.size());

    return roleSet;
  }

  public static String stringfiy(Set<Role> roles) {
    String roleString = Joiner.on(Role.ROLE_SEPARATOR).skipNulls().join(
        roles.stream().map(Role::getName).iterator()
    );

    logger.debug("stringfiy:roles -> [{}]", roleString);
    return roleString;
  }

  public static String stringfiy(Role... roles) {
    return stringfiy(Set.of(roles));
  }

  public static List<GrantedAuthority> toAuthorities(Set<Role> roles) {
    List<GrantedAuthority> list = new ArrayList<>();

    roles.forEach((role) -> list.add(new SimpleGrantedAuthority(Role.ROLE_PREFIX + role.getName())));

    return list;
  }
}

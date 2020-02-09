package ink.o.w.o.resource.role.constant;

import ink.o.w.o.resource.role.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Roles {
    public static final String ROLE_SEPARATOR = "%";
    public final static Role MASTER = new Role().setId(1).setName("MASTER");
    public final static Role USER = new Role().setId(9).setName("USER");
    public final static Role ENDPOINT = new Role().setId(10).setName("ENDPOINT");
    public final static Role RESOURCES = new Role().setId(11).setName("RESOURCES");
    public final static Role RESOURCES_USER = new Role().setId(111).setName("RESOURCES:USER");
    public final static Role RESOURCES_ROLE = new Role().setId(112).setName("RESOURCES:ROLE");
    public final static Role RESOURCES_SAMPLE = new Role().setId(113).setName("RESOURCES:SAMPLE");

    public final static Map<String, Role> ROLES_MAP = Map.of(
        MASTER.getName(), MASTER,
        ENDPOINT.getName(), ENDPOINT,
        RESOURCES.getName(), RESOURCES,
        RESOURCES_USER.getName(), RESOURCES_USER,
        RESOURCES_ROLE.getName(), RESOURCES_ROLE,
        RESOURCES_SAMPLE.getName(), RESOURCES_SAMPLE,
        USER.getName(), USER
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
            roles.stream().map(Role::getName).collect(Collectors.toSet()), ROLE_SEPARATOR
        );

        logger.debug("toRolesString [{}]", roleString);
        return roleString;
    }

    public static String toRolesString(Role... roles) {
        return StringUtils.join(
            Set.of(roles).stream().map(Role::getName), ROLE_SEPARATOR
        );
    }

    public static Set<Role> fromRolesString(String roleString) {
        String[] roleArray =  roleString.split(ROLE_SEPARATOR);
        logger.debug("fromRolesString:roleArray -> {} - {} - {}", roleString, roleArray.length, roleArray[0]);

        Set<Role> roleSet = Set.of(
            roleString.split(ROLE_SEPARATOR)
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
            list.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });

        return list;
    }
}

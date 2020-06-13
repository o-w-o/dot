package ink.o.w.o.resource.system.user.util;

import ink.o.w.o.resource.system.role.domain.Role;
import ink.o.w.o.resource.system.user.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/9 上午9:10
 */

@Slf4j
public class UserHelper {

    public static List<String> getRoles(User u) {
        if (u == null || u.getRoles() == null) {
            return new ArrayList<>();
        }

        return List.of(u.getRoles().stream().map(Role::getName).map(s -> {
            logger.info("ROLE_" + s);
            return "ROLE_" + s;
        }).toArray(String[]::new));
    }

    public static Boolean isExist(User u) {
        return u != null && u.getName() != null;
    }
}

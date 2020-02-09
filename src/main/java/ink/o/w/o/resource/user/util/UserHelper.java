package ink.o.w.o.resource.user.util;

import ink.o.w.o.resource.role.domain.Role;
import ink.o.w.o.resource.user.constant.UserConstant;
import ink.o.w.o.resource.user.domain.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/9 上午9:10
 */
public class UserHelper {

    public static List<String> getRoles(User u) {
        if (u == null || u.getRoles() == null) {
            return new ArrayList<>();
        }

        return List.of(u.getRoles().stream().map(Role::getName).map(s -> {
            System.out.println("ROLE_" + s);
            return "ROLE_" + s;
        }).toArray(String[]::new));
    }

    public static Boolean isExist(User u) {
        return u != null && u.getName() != null;
    }
}

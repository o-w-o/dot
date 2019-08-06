package ink.o.w.o.resource.user.util;

import ink.o.w.o.resource.user.constant.UserConstant;
import ink.o.w.o.resource.user.domain.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/9 上午9:10
 */
public class UserHelper {

    public static List<String> getRoles(User u) {
        List<String> list = new ArrayList<>();
        if (u.getRoles() == null) {
            list.add("USER");
        } else if (u.getRoles().indexOf(UserConstant.USER_ROLE_SEPARATOR) > 0) {
            list.addAll(Arrays.asList(u.getRoles().split(UserConstant.USER_ROLE_SEPARATOR)));
        } else {
            list.add(u.getRoles());
        }

        return list;
    }

    public static Boolean isExist(User u) {
        return u != null && u.getName() != null;
    }
}

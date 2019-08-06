package ink.o.w.o.server.service.impl;

import ink.o.w.o.resource.user.util.UserHelper;
import ink.o.w.o.server.domain.AuthorizedUser;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 用户查询
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/2 下午11:22
 */

@Component
public class AuthorizedUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);

        if (UserHelper.isExist(user)) {
            return AuthorizedUser.parse(user);
        } else {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
    }
}

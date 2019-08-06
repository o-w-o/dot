package ink.o.w.o.server.auth.service.impl;

import ink.o.w.o.server.auth.domain.AuthorizedUser;
import ink.o.w.o.server.user.domain.UserDO;
import ink.o.w.o.server.user.service.UserService;
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
        UserDO user = userService.getUserByUsername(username);

        if (user.isExist()) {
            return AuthorizedUser.parse(user);
        } else {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
    }
}

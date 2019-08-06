package ink.o.w.o.server.auth.service;

import ink.o.w.o.config.domain.ServiceResult;
import ink.o.w.o.server.auth.domain.AuthorizedJwt;
import ink.o.w.o.server.auth.domain.AuthorizedToken;
import ink.o.w.o.server.user.domain.UserDO;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午9:02
 * @version  1.0
 */
public interface AuthorizedUserJwtService {
    /**
     * 用户 token 注册到 redis
     * @author symbols@dingtalk.com
     * @param tokens
     * @param user
     * @return 是否成功
     */
    ServiceResult<Boolean> register(AuthorizedToken tokens, UserDO user);

    /**
     * 用户 token 注销从 redis
     * @author symbols@dingtalk.com
     * @date 2019/8/5 上午9:05
     * @param jwt
     * @return 是否成功
     */
    ServiceResult<Boolean> revoke(AuthorizedJwt jwt);
}

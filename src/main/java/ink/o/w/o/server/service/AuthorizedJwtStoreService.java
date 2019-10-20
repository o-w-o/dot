package ink.o.w.o.server.service;

import ink.o.w.o.server.domain.AuthorizationPayload;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.AuthorizedJwts;
import ink.o.w.o.resource.user.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午9:02
 * @version  1.0
 */
public interface AuthorizedJwtStoreService {
    /**
     * 注册用户 token 到 redis
     * @author symbols@dingtalk.com
     * @since  1.0
     * @date 2019/8/5 上午9:05
     * @param user 待注册用户
     * @return 是否成功
     */
    ServiceResult<AuthorizedJwts> register(User user);

    /**
     * 注销单一用户的单一 token 从 redis
     * @author symbols@dingtalk.com
     * @since  1.0
     * @date 2019/8/5 上午9:05
     * @param jwt jwt
     * @return 是否成功
     */
    ServiceResult<Boolean> revoke(AuthorizedJwt jwt);

    /**
     * 注销单一用户 token 从 redis
     * @author symbols@dingtalk.com
     * @since  1.0
     * @date 2019/10/20 12:00
     * @param userId 用户唯一标识
     * @return 是否成功
     */
    ServiceResult<Boolean> revokeAll(Integer userId);

    /**
     * 注销所有用户 token 从 redis
     * @author symbols@dingtalk.com
     * @since  1.0
     * @date 2019/10/20 12:00
     * @return 是否成功
     */
    ServiceResult<Boolean> reset();
    /**
     * 刷新用户 token 并更新到 redis
     * @author symbols@dingtalk.com
     * @since  1.0
     * @date 2019/10/20 12:00
     * @param jwt token
     * @return 是否成功
     */
    ServiceResult<String> refresh(AuthorizedJwt jwt, String accessToken);

    /**
     * @param request 验证 request
     * @author symbols@dingtalk.com
     * @since  1.0
     * @date 2019/10/19 20:13
     */
    ServiceResult<AuthorizationPayload> validate(HttpServletRequest request);
}

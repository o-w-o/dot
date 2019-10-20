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
     * 用户 token 注册到 redis
     * @author symbols@dingtalk.com
     * @param tokens
     * @param user
     * @return 是否成功
     */
    ServiceResult<Boolean> register(AuthorizedJwts tokens, User user, String jti);

    /**
     * 用户 token 注销从 redis
     * @author symbols@dingtalk.com
     * @date 2019/8/5 上午9:05
     * @param jwt token
     * @return 是否成功
     */
    ServiceResult<Boolean> revoke(AuthorizedJwt jwt);

    /**
     * @param request 待验证的 request
     * @author symbols@dingtalk.com
     * @since  1.0
     * @date 2019/10/19 20:13
     */
    ServiceResult<AuthorizationPayload> validate(HttpServletRequest request);
}

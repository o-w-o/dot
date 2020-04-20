package ink.o.w.o.resource.system.authorization.service;

import ink.o.w.o.resource.system.user.domain.User;
import ink.o.w.o.resource.system.authorization.domain.AuthorizationPayload;
import ink.o.w.o.resource.system.authorization.domain.AuthorizedJwt;
import ink.o.w.o.resource.system.authorization.domain.AuthorizedJwts;
import ink.o.w.o.server.io.service.ServiceResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/5 上午9:02
 */
public interface AuthorizedJwtStoreService {
  /**
   * 注册用户 token 到 redis
   *
   * @param user 待注册用户
   * @return 是否成功
   * @author symbols@dingtalk.com
   * @date 2019/8/5 上午9:05
   * @since 1.0
   */
  ServiceResult<AuthorizedJwts> register(User user);

  /**
   * 注销单一用户的单一 token 从 redis
   *
   * @param jwt jwt
   * @return 是否成功
   * @author symbols@dingtalk.com
   * @date 2019/8/5 上午9:05
   * @since 1.0
   */
  ServiceResult<Boolean> revoke(AuthorizedJwt jwt);

  /**
   * 注销单一用户 token 从 redis
   *
   * @param userId 用户唯一标识
   * @return 是否成功
   * @author symbols@dingtalk.com
   * @date 2019/10/20 12:00
   * @since 1.0
   */
  ServiceResult<Boolean> revokeAll(Integer userId);

  /**
   * 注销所有用户 token 从 redis
   *
   * @return 是否成功
   * @author symbols@dingtalk.com
   * @date 2019/10/20 12:00
   * @since 1.0
   */
  ServiceResult<Boolean> reset();

  /**
   * 刷新用户 token 并更新到 redis
   *
   * @param jwt token
   * @return 是否成功
   * @author symbols@dingtalk.com
   * @date 2019/10/20 12:00
   * @since 1.0
   */
  ServiceResult<String> refresh(AuthorizedJwt jwt, String accessToken);

  /**
   * @param request 验证 request
   * @author symbols@dingtalk.com
   * @date 2019/10/19 20:13
   * @since 1.0
   */
  ServiceResult<AuthorizationPayload> validate(HttpServletRequest request);

  ServiceResult<AuthorizationPayload> validate(String jwt);
}

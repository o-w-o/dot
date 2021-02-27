package o.w.o.domain.core.authorization.service;

import o.w.o.domain.core.authorization.domain.AuthorizationJwt;
import o.w.o.domain.core.authorization.domain.AuthorizedJwt;
import o.w.o.domain.core.user.domain.User;
import o.w.o.infrastructure.definition.ServiceResult;

/**
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/5 上午9:02
 */
public interface AuthorizationStubService {
  /**
   * 注册用户 token 到 redis
   *
   * @param user 待注册用户
   * @return 是否成功
   * @author symbols@dingtalk.com
   * @date 2019/8/5 上午9:05
   * @since 1.0
   */
  ServiceResult<AuthorizedJwt> register(User user);

  /**
   * 刷新用户 token 并更新到 redis
   *
   * @param jwt accessToken
   * @return 是否成功
   * @author symbols@dingtalk.com
   * @date 2019/10/20 12:00
   * @since 1.0
   */
  ServiceResult<AuthorizedJwt> refresh(AuthorizationJwt jwt);

  /**
   * 注销单一用户的单一 token 从 redis
   *
   * @param id -
   * @return 是否成功
   * @author symbols@dingtalk.com
   * @date 2019/8/5 上午9:05
   * @since 1.0
   */
  ServiceResult<Boolean> revoke(String id);

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
}

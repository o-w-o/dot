package o.w.o.resource.system.authorization.service;

import o.w.o.server.definition.ServiceResult;
import o.w.o.resource.system.authorization.domain.AuthorizedJwt;

import java.security.SignatureException;

/**
 * 权限校验服务
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/3 下午2:19
 */
public interface AuthorizationService {
    /**
     * 用户授权
     *
     * @param username 用户名
     * @param password 密码
     * @return jwts {@link AuthorizedJwt}
     */
    ServiceResult<AuthorizedJwt> authorize(String username, String password);

    /**
     * 通过 refreshToken 延长授权
     *
     * @param refreshToken refreshToken
     * @return jwts {@link AuthorizedJwt}
     */
    ServiceResult<AuthorizedJwt> authorize(String refreshToken) throws SignatureException;

    /**
     * 注销用户
     *
     * @param stubId stubId
     * @return revokeStatus
     */
    ServiceResult<Boolean> revoke(String stubId);
}

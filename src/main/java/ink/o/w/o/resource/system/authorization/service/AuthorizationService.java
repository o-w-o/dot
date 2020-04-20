package ink.o.w.o.resource.system.authorization.service;

import ink.o.w.o.resource.system.authorization.domain.AuthorizedJwts;
import ink.o.w.o.server.io.service.ServiceResult;

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
     * @return AuthorizedToken tokens => {'refreshToken':refreshToken, 'accessToken': accessToken}
     */
    ServiceResult<AuthorizedJwts> authorize(String username, String password);

    /**
     * 通过 refreshToken 延长授权
     *
     * @param refreshToken refreshToken
     * @return accessToken
     */
    ServiceResult<String> reauthorize(String refreshToken) throws SignatureException;

    /**
     * 注销用户
     *
     * @param jwt accessToken
     * @return ''
     */
    ServiceResult<Boolean> revoke(String jwt);
}

package ink.o.w.o.resource.system.authorization.service.impl;

import ink.o.w.o.resource.system.authorization.domain.AuthorizedJwt;
import ink.o.w.o.resource.system.authorization.domain.AuthorizedJwts;
import ink.o.w.o.resource.system.authorization.service.AuthorizationService;
import ink.o.w.o.resource.system.authorization.service.AuthorizedJwtStoreService;
import ink.o.w.o.resource.system.user.domain.User;
import ink.o.w.o.resource.system.user.repository.UserRepository;
import ink.o.w.o.server.io.service.ServiceException;
import ink.o.w.o.server.io.service.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 权限校验服务
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/3 下午2:13
 */
@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private AuthorizedJwtStoreService authorizedJwtStoreService;

    @Override
    public ServiceResult<AuthorizedJwts> authorize(String username, String password) {
        if (!userRepository.existsByName(username)) {
            return ServiceResult.error(String.format("用户 [ %s ] 不存在！", username));
        }

        User user = userRepository.findUserByName(username).orElseThrow(new ServiceException(""));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, user.getPassword())) {
            return ServiceResult.success(
                authorizedJwtStoreService.register(user).guard()
            );
        }

        logger.debug("isPasswordMatch ? {}, password -> {}, user -> {}", user.getPassword().equals(password), password, user);
        return ServiceResult.error("登录密码错误！");
    }

    @Override
    public ServiceResult<String> reauthorize(String refreshToken) {
        if (refreshToken == null || "".equals(refreshToken)) {
            return ServiceResult.error("用户 refreshToken 为空！");
        }

        AuthorizedJwt authorizedJwt = AuthorizedJwt.generateJwtFromJwtString(refreshToken, true);

        if (AuthorizedJwt.isExpired(authorizedJwt)) {
            return ServiceResult.error("用户 refreshToken 已过期！");
        }

        return ServiceResult.success(
            authorizedJwtStoreService
                .refresh(authorizedJwt, refreshToken)
                .guard()
        );
    }

    @Override
    public ServiceResult<Boolean> revoke(String accessToken) {
        return authorizedJwtStoreService.revoke(AuthorizedJwt.generateJwtFromJwtString(accessToken, true));
    }
}

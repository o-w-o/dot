package ink.o.w.o.server.service.impl;

import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.repository.UserRepository;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.AuthorizedJwts;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.server.exception.ServiceException;
import ink.o.w.o.server.service.AuthorizationService;
import ink.o.w.o.server.service.AuthorizedJwtStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizedJwtStoreService authorizedJwtStoreService;

    @Override
    public ServiceResult<AuthorizedJwts> authorize(String username, String password) {
        if (!userRepository.existsByName(username)) {
            return ServiceResultFactory.error(String.format("用户 [ %s ] 不存在！", username));
        }

        User user = userRepository.findUserByName(username).orElseThrow(new ServiceException(""));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, user.getPassword())) {
            return ServiceResultFactory.success(
                authorizedJwtStoreService.register(user).guard()
            );
        }

        logger.debug("isPasswordMatch ? {}, password -> {}, user -> {}", user.getPassword().equals(password), password, user);
        return ServiceResultFactory.error("登录密码错误！");
    }

    @Override
    public ServiceResult<String> reauthorize(String refreshToken) {
        if (refreshToken == null || "".equals(refreshToken)) {
            return ServiceResultFactory.error("用户 refreshToken 为空！");
        }

        AuthorizedJwt authorizedJwt = AuthorizedJwt.generateJwtFromJwtString(refreshToken, true);

        if (AuthorizedJwt.isExpired(authorizedJwt)) {
            return ServiceResultFactory.error("用户 refreshToken 已过期！");
        }

        return ServiceResultFactory.success(
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

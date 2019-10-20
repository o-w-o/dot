package ink.o.w.o.server.service.impl;

import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.repository.UserRepository;
import ink.o.w.o.resource.user.util.UserHelper;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.AuthorizedJwts;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.domain.ServiceResultFactory;
import ink.o.w.o.server.service.AuthorizationService;
import ink.o.w.o.server.service.AuthorizedJwtStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;


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
        User user = userRepository.findUserByName(username);
        if (UserHelper.isExist(user)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (user.getPassword().equals(encoder.encode(user.getPassword())) || user.getPassword().equals(user.getPassword())) {
                Date now = new Date();

                AuthorizedJwt jwt = new AuthorizedJwt()
                    .setAud(username)
                    .setRol(user.getRoles())
                    .setUid(user.getId())
                    .setCtime(now)
                    .setUtime(now);

                AuthorizedJwts tokens = new AuthorizedJwts();

                tokens
                    .setAccessToken(AuthorizedJwt.generateJwt(jwt.setExp(DateUtils.addMinutes(now, 15))))
                    .setRefreshToken(AuthorizedJwt.generateJwt(jwt.setExp(DateUtils.addDays(now, 15))));

                authorizedJwtStoreService.register(tokens, user, jwt.getJti());

                return ServiceResultFactory.success(tokens);
            }
        }

        logger.info("isExist ? " + UserHelper.isExist(user) + " " + user);
        return ServiceResultFactory.error("用户 [ " + username + " ] 不存在！");
    }

    @Override
    public ServiceResult<String> reauthorize(String refreshToken) throws SignatureException {
        if ("".equals(refreshToken)) {
            return null;
        }

        String accessToken = AuthorizedJwt.generateJwt(refreshToken, true)
            .setExp(DateUtils.addMinutes(new Date(), 15))
            .toJwt();
        return ServiceResultFactory.success(accessToken);
    }

    @Override
    public ServiceResult<Boolean> revoke(String accessToken) {
        return authorizedJwtStoreService.revoke(new AuthorizedJwt(accessToken)).guard()
            ? ServiceResultFactory.success(true)
            : ServiceResultFactory.success(false);

    }
}

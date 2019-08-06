package ink.o.w.o.server.auth.service.impl;

import ink.o.w.o.server.auth.service.AuthorizationService;
import ink.o.w.o.server.auth.domain.AuthorizedJwt;
import ink.o.w.o.server.auth.domain.AuthorizedToken;
import ink.o.w.o.server.user.domain.UserDO;
import ink.o.w.o.server.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private RedisTemplate redisTemplate;

    @Override
    public AuthorizedToken authorize(String username, String password) {
        UserDO user = userRepository.findUserByName(username);

        if (user == null ? false : user.isExist()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (user.getPassword().equals(encoder.encode(user.getPassword())) || user.getPassword().equals(user.getPassword())) {
                Date now = new Date();

                AuthorizedJwt jwt = new AuthorizedJwt()
                    .setAud(username)
                    .setRol(user.getRoles())
                    .setUid(user.getId())
                    .setCtime(now)
                    .setUtime(now);

                AuthorizedToken tokens = new AuthorizedToken();

                tokens
                    .setAccessToken(AuthorizedJwt.generateJwt(jwt.setExp(DateUtils.addMinutes(now, 15))))
                    .setRefreshToken(AuthorizedJwt.generateJwt(jwt.setExp(DateUtils.addDays(now, 15))));

                return tokens;
            }
        }

        return null;

    }

    @Override
    public String reauthorize(String refreshToken) throws SignatureException {
        if ("".equals(refreshToken)) {
            return null;
        }

        String accessToken = AuthorizedJwt.generateJwt(refreshToken, true)
            .setExp(DateUtils.addMinutes(new Date(), 15))
            .toJwt();
        return accessToken;
    }

    @Override
    public Boolean revoke(String jwt) {

        String name = AuthorizedJwt.getClaimsFromJwt(jwt).getAudience();
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);

        return true;
    }
}

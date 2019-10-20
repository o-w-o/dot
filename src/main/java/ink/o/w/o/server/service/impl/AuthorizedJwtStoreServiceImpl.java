package ink.o.w.o.server.service.impl;

import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.server.domain.*;
import ink.o.w.o.server.repository.AuthorizedJwtStoreRepository;
import ink.o.w.o.server.service.AuthorizedJwtStoreService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class AuthorizedJwtStoreServiceImpl implements AuthorizedJwtStoreService {
    @Autowired
    AuthorizedJwtStoreRepository authorizedJwtStoreRepository;

    @Override
    public ServiceResult<Boolean> register(AuthorizedJwts tokens, User user, String jti) {
        authorizedJwtStoreRepository.save(
            new AuthorizedJwtStore()
                .setId(AuthorizedJwtStore.generateUuid(user.getName(), jti))
                .setAuthorizedJwts(tokens)
        );
        return ServiceResultFactory.success(true);
    }

    @Override
    public ServiceResult<Boolean> revoke(AuthorizedJwt jwt) {
        authorizedJwtStoreRepository.deleteById(AuthorizedJwtStore.generateUuid(jwt.getAud(), jwt.getJti()));
        return ServiceResultFactory.success(true);
    }

    @Override
    public ServiceResult<AuthorizationPayload> validate(HttpServletRequest request) {
        AuthorizationPayload authorizationPayload = new AuthorizationPayload();

        ServiceResult<AuthorizationPayload> serviceResult = new ServiceResult<>();
        serviceResult.setPayload(authorizationPayload);

        String httpHeader = request.getHeader(AuthorizedJwt.REQUEST_AUTHORIZATION_KEY);
        authorizationPayload.setJwtHeaderEmpty(httpHeader == null || httpHeader.isEmpty());

        logger.info("HTTP 头部 是否携带 Token ? " + !authorizationPayload.isJwtHeaderEmpty());
        if (authorizationPayload.isJwtHeaderEmpty()) {
            return serviceResult.setMessage("HTTP 头部 未携带 Token ！").setSuccess(false);
        }

        authorizationPayload.setJwtHeaderValid(httpHeader.startsWith(AuthorizedJwt.AUTHORIZATION_PREFIX));

        logger.info("HTTP 头部 -" + AuthorizedJwt.REQUEST_AUTHORIZATION_KEY + "- 字段值是否以 [" + AuthorizedJwt.AUTHORIZATION_PREFIX + "] 开始 ? " + httpHeader.startsWith(AuthorizedJwt.AUTHORIZATION_PREFIX));
        if (!authorizationPayload.isJwtHeaderValid()) {
            return serviceResult.setMessage("HTTP 头部 格式错误 ！").setSuccess(false);
        }

        logger.info("用户授权检验……");

        String jwt = httpHeader
            .substring(AuthorizedJwt.AUTHORIZATION_PREFIX.length());


        try {
            authorizationPayload.setJwtClaims(AuthorizedJwt.getClaimsFromJwt(jwt));
            authorizationPayload.setJwtParsePassed(true);
        } catch (Exception e) {
            authorizationPayload.setJwtParsePassed(false);
        }

        if (!authorizationPayload.isJwtParsePassed()) {
            logger.info("用户授权信息不足，授权终止！");
            return serviceResult.setMessage("用户授权信息不足，授权终止！").setSuccess(false);
        }

        logger.info("用户 TOKEN 检验……");

        // 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
        // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
        // 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
        // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
        Claims jwtClaims = authorizationPayload.getJwtClaims();
        if (!authorizedJwtStoreRepository.existsById(
            AuthorizedJwtStore.generateUuid(
                jwtClaims.getAudience(),
                jwtClaims.getId()
            ))) {
            logger.info("用户 TOKEN 检验未通过，服务端存储异常！");
            return serviceResult.setMessage("用户 TOKEN 检验未通过，服务端存储异常！").setSuccess(false);
        }

        authorizationPayload.setJwtValid(AuthorizedJwt.valid(authorizationPayload.getJwtClaims()));
        if (authorizationPayload.isJwtValid()) {
            logger.info("用户 TOKEN 检验通过！");
            return serviceResult.setPayload(authorizationPayload).setSuccess(true);
        }

        logger.info("用户 TOKEN 检验未通过！");
        return serviceResult.setMessage("用户 TOKEN 检验未通过！").setSuccess(false);

    }
}

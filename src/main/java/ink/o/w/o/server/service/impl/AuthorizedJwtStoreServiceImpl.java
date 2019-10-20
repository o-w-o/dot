package ink.o.w.o.server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.server.domain.*;
import ink.o.w.o.server.repository.AuthorizedJwtStoreRepository;
import ink.o.w.o.server.service.AuthorizedJwtStoreService;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AuthorizedJwtStoreServiceImpl implements AuthorizedJwtStoreService {
    @Autowired
    AuthorizedJwtStoreRepository authorizedJwtStoreRepository;

    @Autowired
    JSONHelper jsonHelper;

    @Override
    public ServiceResult<AuthorizedJwts> register(User user) {
        Date now = new Date();

        AuthorizedJwt authorizedJwt = new AuthorizedJwt()
            .setAud(user.getName())
            .setRol(user.getRoles())
            .setUid(user.getId())
            .setCtime(now)
            .setUtime(now);

        AuthorizedJwts authorizedJwts = new AuthorizedJwts();

        authorizedJwts
            .setAccessToken(AuthorizedJwt.generateJwt(authorizedJwt.setExp(DateUtils.addMinutes(now, 15))))
            .setRefreshToken(AuthorizedJwt.generateJwt(authorizedJwt.setExp(DateUtils.addDays(now, 15))));

        try {
            logger.info(" ->" + jsonHelper.toJSONString(authorizedJwts));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        authorizedJwtStoreRepository.save(
            new AuthorizedJwtStore()
                .setId(AuthorizedJwtStore.generateUuid(authorizedJwt))
                .setUserId(user.getId())
                .setAuthorizedJwts(authorizedJwts)
        );

        return ServiceResultFactory.success(authorizedJwts);
    }

    @Override
    public ServiceResult<Boolean> revoke(AuthorizedJwt jwt) {
        authorizedJwtStoreRepository.deleteById(AuthorizedJwtStore.generateUuid(jwt));
        return ServiceResultFactory.success(true);
    }

    @Override
    public ServiceResult<Boolean> revokeAll(Integer userId) {
        authorizedJwtStoreRepository.deleteByUserId(userId);
        return ServiceResultFactory.success(true);
    }

    @Override
    public ServiceResult<Boolean> reset() {
        authorizedJwtStoreRepository.deleteAll();
        return ServiceResultFactory.success(true);
    }

    @Override
    public ServiceResult<String> refresh(AuthorizedJwt jwt, String refreshToken) {
        String id = AuthorizedJwtStore.generateUuid(jwt);
        Optional<AuthorizedJwtStore> authorizedJwtStoreOptional = authorizedJwtStoreRepository.findById(id);

        if (authorizedJwtStoreOptional.isEmpty()) {
            return ServiceResultFactory.error("用户 refreshToken 无效，未查到有效 accessToken！" + id);
        } else {
            authorizedJwtStoreRepository.deleteById(id);
        }

        AuthorizedJwtStore authorizedJwtStore = authorizedJwtStoreOptional.get();

        authorizedJwtStoreRepository.save(
            authorizedJwtStore.setAuthorizedJwts(
                authorizedJwtStore
                    .getAuthorizedJwts()
                    .setAccessToken(
                        new AuthorizedJwt(authorizedJwtStore.getAuthorizedJwts().getAccessToken(), true)
                            .setExp(DateUtils.addMinutes(new Date(), 15))
                            .toJwt()
                    )
            )
        );

        return ServiceResultFactory.success(
            authorizedJwtStoreRepository.findById(id)
                .get()
                .getAuthorizedJwts()
                .getAccessToken()
        );
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

        AuthorizedJwt authorizedJwt = new AuthorizedJwt(authorizationPayload.getJwtClaims(), true);
        if (!authorizedJwtStoreRepository.existsById(AuthorizedJwtStore.generateUuid(authorizedJwt))) {
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

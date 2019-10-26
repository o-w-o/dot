package ink.o.w.o.server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.server.domain.*;
import ink.o.w.o.server.exception.ServiceException;
import ink.o.w.o.server.repository.AuthorizedJwtStoreRepository;
import ink.o.w.o.server.service.AuthorizedJwtStoreService;
import ink.o.w.o.util.JSONHelper;
import io.jsonwebtoken.Claims;
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
            .setIat(now);

        AuthorizedJwts authorizedJwts = new AuthorizedJwts();

        authorizedJwts
            .setAccessToken(authorizedJwt.setExp(DateUtils.addMinutes(now, 15)).toString())
            .setRefreshToken(authorizedJwt.setExp(DateUtils.addDays(now, 15)).toString());

        // TODO [ REMOVE DEBUG CODE ]
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
        AuthorizedJwtStore authorizedJwtStore = authorizedJwtStoreRepository
            .findById(id)
            .orElseThrow(new ServiceException("用户 refreshToken 无效，未查到有效 accessToken！" + id));

        authorizedJwtStoreRepository.deleteById(id);

        authorizedJwtStoreRepository.save(
            authorizedJwtStore.setAuthorizedJwts(
                authorizedJwtStore
                    .getAuthorizedJwts()
                    .setAccessToken(
                        AuthorizedJwt.generateJwtFromJwtString(authorizedJwtStore.getAuthorizedJwts().getAccessToken(), true)
                            .setExp(DateUtils.addMinutes(new Date(), 15))
                            .toString()
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

        String httpHeader = request.getHeader(AuthorizedJwt.AUTHORIZATION_HEADER_KEY);
        authorizationPayload.setJwtHeaderEmpty(Optional.ofNullable(httpHeader).isEmpty());

        logger.info("HTTP 头部 是否携带 Token ? " + !authorizationPayload.isJwtHeaderEmpty());
        if (authorizationPayload.isJwtHeaderEmpty()) {
            return serviceResult.setMessage("HTTP 头部 未携带 Token ！").setSuccess(false);
        }

        authorizationPayload.setJwtHeaderValid(httpHeader.startsWith(AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX));

        logger.info("HTTP 头部 -" + AuthorizedJwt.AUTHORIZATION_HEADER_KEY + "- 字段值是否以 [" + AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + "] 开始 ? " + httpHeader.startsWith(AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX));
        if (!authorizationPayload.isJwtHeaderValid()) {
            return serviceResult.setMessage("HTTP 头部 格式错误 ！").setSuccess(false);
        }

        logger.info("用户授权检验……");

        String jwt = httpHeader
            .substring(AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX.length());


        try {
            Claims claims = AuthorizedJwt.parseClaimsFromJwtString(jwt);
            AuthorizedJwt authorizedJwt = AuthorizedJwt.generateJwtFromClaims(claims, true);
            authorizationPayload.setJwt(authorizedJwt);
            authorizationPayload.setJwtClaims(claims);
            authorizationPayload.setJwtParsePassed(true);
        } catch (Exception e) {
            authorizationPayload.setJwtParsePassed(false);
        }

        if (!authorizationPayload.isJwtParsePassed()) {
            logger.info("用户授权信息不足，授权终止！");
            return serviceResult.setMessage("用户授权信息不足，授权终止！").setSuccess(false);
        }

        logger.info("用户 TOKEN 检验……");

        AuthorizedJwt authorizedJwt = AuthorizedJwt.generateJwtFromClaims(authorizationPayload.getJwtClaims(), true);
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

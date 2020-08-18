package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.domain.AuthorizedJwt;
import o.w.o.resource.system.authorization.domain.AuthorizedJwts;
import o.w.o.resource.system.authorization.service.AuthorizationService;
import o.w.o.server.io.api.APIContext;
import o.w.o.server.io.api.APIException;
import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.APISchemata;
import o.w.o.server.io.api.annotation.APIResource;
import o.w.o.server.io.api.annotation.APIResourceCreate;
import o.w.o.server.io.api.annotation.APIResourceDestroy;
import o.w.o.server.io.api.annotation.APIResourceSchema;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.SignatureException;


/**
 * 授权 API
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/10 11:44
 * @since 1.0.0
 */
@Slf4j
@RestController
@APIResource(path = "authorization")
public class AuthorizationAPI {
  @Resource
  private AuthorizationService authorizationService;

  @APIResourceSchema
  public APIResult<APISchemata> fetchSchema() {
    return APIResult.of(APIContext.fetchAPIContext(AuthorizationAPI.class).orElseThrow(APIException::new));
  }


  @APIResourceCreate(name = "创建令牌")
  public APIResult<?> createAuthenticationToken(
      @RequestParam(name = "username") String username,
      @RequestParam(name = "password") String password) throws AuthenticationException {

    var jwts = authorizationService.authorize(username, password)
        .guard();

    return APIResult.of(jwts);
  }

  @APIResourceCreate(path = "/refresh", name = "刷新令牌")
  @PreAuthorize("hasRole('ROLE_USER')")
  public APIResult<?> refreshAuthenticationToken(
      @RequestParam(name = "refreshToken") String refreshToken) throws AuthenticationException, SignatureException {

    return APIResult.of(
        new AuthorizedJwts()
            .setAccessToken(authorizationService.reauthorize(refreshToken).guard())
            .setRefreshToken(refreshToken)
    );
  }

  @APIResourceDestroy(path = "/revoke", name = "注销令牌", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_USER')")
  public APIResult<?> revokeAuthenticationToken(
      @RequestHeader(name = AuthorizedJwt.AUTHORIZATION_HEADER_KEY) String jwt
  ) throws AuthenticationException {
    var result = authorizationService.revoke(jwt.substring(AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX.length()));
    return APIResult.from(result, "注销成功");
  }
}

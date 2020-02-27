package ink.o.w.o.api;

import ink.o.w.o.resource.aliyun.service.AliyunStsService;
import ink.o.w.o.resource.aliyun.factory.PolicyFactory;
import ink.o.w.o.resource.authorization.domain.AuthorizedJwt;
import ink.o.w.o.resource.authorization.domain.AuthorizedJwts;
import ink.o.w.o.resource.authorization.service.AuthorizationService;
import ink.o.w.o.server.domain.ResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.QueryParameter;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

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
@ExposesResourceFor(AuthorizationAPI.class)
@RequestMapping(value = "authorization")
public class AuthorizationAPI {
  private final EntityLinks entityLinks;
  private final AuthorizationService authorizationService;
  private final AliyunStsService aliyunStsService;

  @Autowired
  AuthorizationAPI(EntityLinks entityLinks, AuthorizationService authorizationService, AliyunStsService aliyunStsService) {
    this.entityLinks = entityLinks;
    this.authorizationService = authorizationService;
    this.aliyunStsService = aliyunStsService;
  }

  @PostMapping
  public ResponseEntity<?> createAuthenticationToken(
      @RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password) throws AuthenticationException {

    var jwts = authorizationService.authorize(username, password)
        .guard();

    return ResponseEntity.ok(
        new EntityModel<>(
            jwts,

            entityLinks
                .linkFor(AuthorizationAPI.class).withSelfRel()
                .withTitle("令牌获取")
                .withName("fetch"),
            entityLinks
                .linkFor(AuthorizationAPI.class, QueryParameter.required("refreshToken")).slash("refresh").withRel("refresh")
                .withTitle("令牌刷新")
                .withName("refresh"),
            entityLinks
                .linkFor(AuthorizationAPI.class).slash("revoke").withRel("revoke")
                .withTitle("令牌注销")
                .withName("revoke")
        )
    );
  }

  @PostMapping("/refresh")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> refreshAuthenticationToken(
      @RequestParam String refreshToken) throws AuthenticationException, SignatureException {

    return ResponseEntity.ok(
        new EntityModel<>(
            new AuthorizedJwts()
                .setAccessToken(authorizationService.reauthorize(refreshToken).guard())
                .setRefreshToken(refreshToken),

            entityLinks
                .linkFor(AuthorizationAPI.class).slash("refresh").withSelfRel()
                .withTitle("令牌刷新")
                .withName("refresh"),
            entityLinks
                .linkFor(AuthorizationAPI.class).slash("revoke").withRel("revoke")
                .withTitle("令牌注销")
                .withName("revoke")
        )
    );
  }

  @DeleteMapping(value = "/revoke", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> revokeAuthenticationToken(
      @RequestHeader(name = AuthorizedJwt.AUTHORIZATION_HEADER_KEY) String jwt
  ) throws AuthenticationException {
    var result = authorizationService.revoke(jwt.substring(AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX.length()));
    return result.guard()
        ? ResponseEntityFactory.ok("注销成功")
        : ResponseEntityFactory.generateFrom(result);
  }

  @PostMapping(value = "/sts", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> createSts() {
    var result = aliyunStsService.createStsCredentialsForUser(PolicyFactory.Preset.User_ReadOnly);
    return  ResponseEntityFactory.success(result.guard());
  }

  @PostMapping(value = "/sts/somebody", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> createStsSomebody() {
    var result = aliyunStsService.createStsCredentialsForUser(PolicyFactory.Preset.User_ReadAndWrite);
    return ResponseEntityFactory.success(result.guard());
  }

  @PostMapping(value = "/sts/nobody", produces = "application/json")
  public ResponseEntity<?> createStsNobody() {
    var result = aliyunStsService.createStsCredentialsForAnonymous();
    return ResponseEntityFactory.success(result.guard());
  }
}

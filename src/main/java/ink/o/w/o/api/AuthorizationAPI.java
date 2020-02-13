package ink.o.w.o.api;

import ink.o.w.o.server.constant.HttpConstant;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.AuthorizedJwts;
import ink.o.w.o.server.service.AuthorizationService;
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
@RequestMapping(HttpConstant.API_BASE_URL + "/authorization")
public class AuthorizationAPI {
  @Autowired
  EntityLinks entityLinks;

  @Autowired
  private AuthorizationService authorizationService;

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

  @PostMapping(value = "/revoke", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> revokeAuthenticationToken(
      @RequestHeader(name = AuthorizedJwt.AUTHORIZATION_HEADER_KEY) String jwt
  ) throws AuthenticationException {

    return ResponseEntity.ok(authorizationService.revoke(jwt).guard() ? "注销成功" : "-");
  }
}

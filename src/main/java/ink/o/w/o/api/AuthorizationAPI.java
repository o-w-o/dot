package ink.o.w.o.api;

import ink.o.w.o.server.constant.HttpConstant;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.AuthorizedJwts;
import ink.o.w.o.server.domain.HttpResponseData;
import ink.o.w.o.server.domain.HttpResponseDataFactory;
import ink.o.w.o.server.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.QueryParameter;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


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
@RequestMapping("/authorization")
public class AuthorizationAPI {
  @Autowired
  EntityLinks entityLinks;

  @Autowired
  private AuthorizationService authorizationService;

  @GetMapping("")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestParam String username,
      @RequestParam String password) throws AuthenticationException, SignatureException {

    var jwts = authorizationService.authorize(username, password)
        .guard();

    var link = entityLinks
        .linkFor(AuthorizationAPI.class).withSelfRel()
        .withTitle("令牌获取")
        .withName("fetch")
        .andAffordance(
            afford(methodOn(getClass()).refreshAuthenticationToken(jwts.getRefreshToken()))
        )
        .withName("refresh")
        .andAffordance(
            afford(methodOn(getClass()).revokeAuthenticationToken(jwts.getAccessToken()))
        )
        .withName("revoke");

    return ResponseEntity.ok(
        new EntityModel<>(
            jwts,
            link,
            entityLinks
                .linkFor(AuthorizationAPI.class, QueryParameter.required("refreshToken")).withRel("refresh")
                .withTitle("令牌刷新")
                .withName("refresh"),
            entityLinks
                .linkFor(AuthorizationAPI.class).withRel("revoke")
                .withTitle("令牌注销")
                .withName("revoke")
        )
    );
  }

  @PostMapping("/tokens")
  @PreAuthorize("hasRole('ROLE_USER')")
  public HttpResponseData refreshAuthenticationToken(
      @RequestParam String refreshToken) throws AuthenticationException, SignatureException {

    return HttpResponseDataFactory.success(
        authorizationService.reauthorize(refreshToken)
            .guard()
    );
  }

  @DeleteMapping(value = "/tokens", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public HttpResponseData revokeAuthenticationToken(
      @RequestHeader(name = AuthorizedJwt.AUTHORIZATION_HEADER_KEY) String jwt
  ) throws AuthenticationException {

    return authorizationService.revoke(jwt).guard()
        ? HttpResponseDataFactory.success("注销成功")
        : HttpResponseDataFactory.error("注销失败");
  }
}

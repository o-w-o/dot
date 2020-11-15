package o.w.o.api;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.domain.AuthorizedJwt;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import o.w.o.resource.system.authorization.service.AuthorizationService;
import o.w.o.server.definition.ApiResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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
@RequestMapping("authorization")
public class AuthorizationApi {
  @Resource
  private AuthorizationService authorizationService;


  @PostMapping
  @ApiOperation(value = "创建令牌")
  public ApiResult<AuthorizedJwt> createAuthenticationToken(
      @RequestParam(name = "username") String username,
      @RequestParam(name = "password") String password) throws AuthenticationException {

    return ApiResult.of(
        authorizationService.authorize(username, password)
            .guard()
    );
  }

  @PostMapping("refresh")
  @ApiOperation("刷新令牌")
  public ApiResult<AuthorizedJwt> refreshAuthenticationToken(
      @RequestParam(name = "refreshToken") @NotNull String refreshToken) throws AuthenticationException, SignatureException {

    return ApiResult.of(
        authorizationService.authorize(refreshToken)
            .guard()
    );
  }

  @DeleteMapping("revoke")
  @ApiOperation("注销令牌")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ApiResult<Boolean> revokeAuthenticationToken(
      @AuthenticationPrincipal AuthorizedUser user
  ) throws AuthenticationException {
    var result = authorizationService.revoke(user.getStubId());
    return ApiResult.from(result, "注销成功");
  }
}

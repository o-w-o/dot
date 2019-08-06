package ink.o.w.o.api;

import ink.o.w.o.config.constant.HttpConstant;
import ink.o.w.o.config.domain.HttpResponseData;
import ink.o.w.o.config.domain.HttpResponseDataFactory;
import ink.o.w.o.server.auth.domain.AuthorizedJwt;
import ink.o.w.o.server.auth.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@RestController
@RequestMapping(HttpConstant.API_ENTRY + "/auth")
public class AuthorizationAPI {

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/token")
    public HttpResponseData createAuthenticationToken(
        @RequestParam String username,
        @RequestParam String password) throws AuthenticationException {

        return HttpResponseDataFactory.success(authorizationService.authorize(username, password));
    }

    @PostMapping("/token")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpResponseData refreshAuthenticationToken(
        @RequestParam String refreshToken) throws AuthenticationException, SignatureException {

        return HttpResponseDataFactory.success(authorizationService.reauthorize(refreshToken));
    }

    @DeleteMapping(value = "/auth", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpResponseData refreshAndGetAuthenticationToken(
        @RequestHeader(name = AuthorizedJwt.REQUEST_AUTHORIZATION_KEY) String jwt
    ) throws AuthenticationException {

        boolean result = authorizationService.revoke(jwt);

        return result
            ? HttpResponseDataFactory.success("注销成功")
            : HttpResponseDataFactory.error("注销失败");
    }
}

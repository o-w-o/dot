package ink.o.w.o.api;

import ink.o.w.o.server.constant.HttpConstant;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.HttpResponseData;
import ink.o.w.o.server.domain.HttpResponseDataFactory;
import ink.o.w.o.server.domain.ServiceResult;
import ink.o.w.o.server.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@Slf4j
@RestController
@RequestMapping(HttpConstant.API_ENTRY + "/auth")
public class AuthorizationAPI {

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/token")
    public HttpResponseData createAuthenticationToken(
        @RequestParam String username,
        @RequestParam String password) throws AuthenticationException {

        return HttpResponseDataFactory.success(
            authorizationService.authorize(username, password)
                .guard()
        );
    }

    @PostMapping("/token")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpResponseData refreshAuthenticationToken(
        @RequestParam String refreshToken) throws AuthenticationException, SignatureException {

        return HttpResponseDataFactory.success(
            authorizationService.reauthorize(refreshToken)
                .guard()
        );
    }

    @DeleteMapping(value = "/auth", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpResponseData revokeAuthenticationToken(
        @RequestHeader(name = AuthorizedJwt.REQUEST_AUTHORIZATION_KEY) String jwt
    ) throws AuthenticationException {

        return authorizationService.revoke(jwt).guard()
            ? HttpResponseDataFactory.success("注销成功")
            : HttpResponseDataFactory.error("注销失败");
    }
}

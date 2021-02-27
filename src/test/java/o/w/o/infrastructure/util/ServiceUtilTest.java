package o.w.o.infrastructure.util;

import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceUtilTest {
  public static void mockAuthorize(AuthorizedUser authorizedUser) {
    var authentication = new TestingAuthenticationToken(
        authorizedUser,
        authorizedUser.getAuthorities()
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public static void mockAuthorize() {
    var authorizedUser = AuthorizedUser.createAnonymousUser("127.0.0.1");
    var authentication = new TestingAuthenticationToken(
        authorizedUser,
        authorizedUser.getAuthorities()
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
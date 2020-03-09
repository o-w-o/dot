package ink.o.w.o.util;

import ink.o.w.o.resource.authorization.domain.AuthorizedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServerHelper {
  public AuthorizedUser getAuthorizedUserFormSecurityContext() {
    return (AuthorizedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public Integer getUserIdFormSecurityContext() {
    return getAuthorizedUserFormSecurityContext().getId();
  }

  public Optional<AuthorizedUser> fetchAuthorizedUserFormSecurityContext() {
    return Optional
        .ofNullable(
            SecurityContextHolder
                .getContext()
                .getAuthentication()
        )
        .map(Authentication::getPrincipal)
        .map((u) -> (AuthorizedUser) u);
  }

  public Optional<Integer> fetchUserIdFormSecurityContext() {
    return fetchAuthorizedUserFormSecurityContext().map(AuthorizedUser::getId);
  }
}

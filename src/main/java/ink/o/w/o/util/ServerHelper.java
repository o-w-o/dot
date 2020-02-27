package ink.o.w.o.util;

import ink.o.w.o.resource.authorization.domain.AuthorizedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ServerHelper {
  public AuthorizedUser getAuthorizedUserFormSecurityContext() {
    return (AuthorizedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public Integer getUserIdFormSecurityContext() {
    return getAuthorizedUserFormSecurityContext().getId();
  }
}

package ink.o.w.o.server.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ServerHelper {
  public static Authentication getAuthenticationFormSecurityContext() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static Integer getUserIdFormSecurityContext() {
    return Integer.parseInt(getAuthenticationFormSecurityContext().getName());
  }
}

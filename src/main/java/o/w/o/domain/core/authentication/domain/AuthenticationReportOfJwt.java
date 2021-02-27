package o.w.o.domain.core.authentication.domain;

import lombok.Data;
import o.w.o.domain.core.authorization.domain.AuthorizationJwt;

@Data
public class AuthenticationReportOfJwt {
  private String jwts;
  private AuthorizationJwt jwt;

  private boolean jwtNonExpired = false;
  private boolean jwtParsed = false;
  private boolean jwtValid = false;

  private String message;
}

package o.w.o.resource.system.authorization.domain.property;

import org.springframework.http.HttpHeaders;

public class AuthorizationHeader {

  public static final String HEADER_KEY = HttpHeaders.AUTHORIZATION;
  public static final String HEADER_VAL_PREFIX = "JWT.";

}

package ink.o.w.o.server.domain;

import io.jsonwebtoken.Claims;
import lombok.Data;

@Data
public class AuthorizationPayload {
    private AuthorizedJwt jwt;
    private Claims jwtClaims;
    private boolean jwtHeaderEmpty = false;
    private boolean jwtHeaderValid = false;
    private boolean jwtParsePassed = false;
    private boolean jwtValid = false;
}

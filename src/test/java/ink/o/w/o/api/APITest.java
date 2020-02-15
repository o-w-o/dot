package ink.o.w.o.api;

import ink.o.w.o.api.config.RestDocumentTestConfiguration;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.AuthorizedJwts;
import ink.o.w.o.server.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/8 上午8:59
 */

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriHost = "api.o-w-o.ink", uriScheme = "https", uriPort = 443)
@Import(RestDocumentTestConfiguration.class)
public class APITest {
  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected RestDocumentationResultHandler restDocumentationResultHandler;

  protected final String authorizationBaseUrl = "/authorization";

  @Autowired
  private AuthorizationService authorizationService;

  public static String getAuthorizationHeaderKey() {
    return AuthorizedJwt.AUTHORIZATION_HEADER_KEY;
  }

  public static String getAuthorizationHeaderValue(String accessToken) {
    return AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + accessToken;
  }

  public AuthorizedJwts getAuthorization(String username, String password) {
    return authorizationService.authorize("demo", "233333").guard();
  }
}

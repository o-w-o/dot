package o.w.o.api.system;

import lombok.extern.slf4j.Slf4j;
import o.w.o.api.APITest;
import o.w.o.resource.system.authorization.service.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.apache.commons.codec.CharEncoding.UTF_8;

@AutoConfigureCache
@AutoConfigureDataRedis
@AutoConfigureDataJpa
@Slf4j
public class UserAPITest extends APITest {

  private final String userBaseUrl = "/users";
  @Autowired
  private AuthorizationService authorizationService;
  private String accessToken;

  @BeforeEach
  public void initAuthorization() {
    this.accessToken = this.authorizationService.authorize("qa", "233333").guard().getAccessToken();
  }

  @Test
  public void isUserGetOk() throws Exception {
    this.mockMvc.perform(
        RestDocumentationRequestBuilders
            .get(String.format("%s/%s", this.userBaseUrl, 1))
            .header(APITest.getAuthorizationHeaderKey(), APITest.getAuthorizationHeaderValue(this.accessToken))
            .characterEncoding(UTF_8)
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}

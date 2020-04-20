package ink.o.w.o.api;

import ink.o.w.o.resource.system.authorization.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.apache.commons.codec.CharEncoding.UTF_8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureCache
@AutoConfigureDataRedis
@AutoConfigureDataJpa
@Slf4j
public class MyAPITest extends APITest {

  private final String userBaseUrl = "/my";
  @Autowired
  private AuthorizationService authorizationService;
  private String accessToken;

  @BeforeEach
  public void initAuthorization() {
    accessToken = authorizationService.authorize("qa", "233333").guard().getAccessToken();
  }


  @Test
  public void isMyGetOk() throws Exception {
    mockMvc.perform(
        RestDocumentationRequestBuilders
            .get(userBaseUrl)
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(accessToken))
            .characterEncoding(UTF_8)
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());
  }
}

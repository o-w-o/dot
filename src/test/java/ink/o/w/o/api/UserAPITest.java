package ink.o.w.o.api;

import ink.o.w.o.server.constant.HttpConstant;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class UserAPITest extends APITest {

  private final String userBaseUrl = HttpConstant.API_BASE_URL + "/users";
  @Autowired
  private AuthorizationService authorizationService;
  private String accessToken;

  @BeforeEach
  public void initAuthorization() {
    accessToken = authorizationService.authorize("qa", "233333").guard().getAccessToken();
  }

  @Test
  public void isUserGetOk() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders
            .get(String.format("%s/%s", userBaseUrl, 1))
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + accessToken)
            .accept(MediaTypes.HAL_JSON)
    )
        .andExpect(status().isOk());
  }
}

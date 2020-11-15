package o.w.o.api.system;

import lombok.extern.slf4j.Slf4j;
import o.w.o.api.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class UserApiTest extends ApiTest {
  private final String userBaseUrl = "/users";

  @Test
  public void isUserGetOk() throws Exception {
    this.mockMvc
        .perform(
            MockMvcRequestBuilders
                .get(String.format("%s/%s", this.userBaseUrl, qa.getId()))
                .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(qaJwt.getAccessToken()))
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
  }
}

package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.authorization.domain.AuthorizedJwt;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/3 上午9:52
 */

@Slf4j
public class AuthorizationApiTest extends ApiTest {

  @Test
  public void authenticationFlow() throws Exception {
    String url = String.format("%s?username=%s&password=%s", this.authorizationBaseUrl, ApiTest.QA_NAME, ApiTest.QA_PASSWORD);

    var result = this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(url)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("payload.accessToken", is(notNullValue())))
        .andExpect(jsonPath("payload.refreshToken", is(notNullValue())))
        .andReturn()
        .getResponse()
        .getContentAsString();

    var jwt = jsonHelper.readApiResultPayload(result, AuthorizedJwt.class).orElseThrow();

    var refreshedResult = this.mockMvc
        .perform(
            MockMvcRequestBuilders
                .post(String.format("%s/refresh?refreshToken=%s", this.authorizationBaseUrl, jwt.getRefreshToken()))
                .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(jwt.getAccessToken()))
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("payload.accessToken", is(notNullValue())))
        .andExpect(jsonPath("payload.refreshToken", is(notNullValue())))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();


    var refreshedJwt = jsonHelper.readApiResultPayload(refreshedResult, AuthorizedJwt.class).orElseThrow();

    this.mockMvc.perform(
        MockMvcRequestBuilders
            .delete(String.format("%s/revoke", this.authorizationBaseUrl))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(refreshedJwt.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());


    this.mockMvc.perform(
        MockMvcRequestBuilders
            .delete(String.format("%s/revoke", this.authorizationBaseUrl))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(refreshedJwt.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void isTokenCreatedErrorParameter() throws Exception {
    String url = String.format("%s?username=%s", this.authorizationBaseUrl, ApiTest.QA_NAME);

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(url)
        )
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void isTokenCreatedErrorPassword() throws Exception {
    String url = String.format("%s?username=%s&password=%s", this.authorizationBaseUrl, ApiTest.QA_NAME, ApiTest.QA_PASSWORD + "xxx");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void isTokenCreatedErrorUsername() throws Exception {
    String url = String.format("%s?username=%s&password=%s", this.authorizationBaseUrl, ApiTest.QA_NAME + "xxx", ApiTest.QA_PASSWORD);

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }
}

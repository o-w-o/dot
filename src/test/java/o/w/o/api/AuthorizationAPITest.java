package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.domain.AuthorizedJwts;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/3 上午9:52
 */

@Slf4j
public class AuthorizationAPITest extends APITest {

  @Test
  public void isTokenCreated() throws Exception {
    String url = this.authorizationBaseUrl + "?username=demo&password=233333";

    this.mockMvc
        .perform(RestDocumentationRequestBuilders
            .post(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("payload.accessToken", is(notNullValue())))
        .andExpect(jsonPath("payload.refreshToken", is(notNullValue())))
        .andDo(
            this.restDocumentationResultHandler.document(
                requestParameters(
                    parameterWithName("username").description("用户名"),
                    parameterWithName("password").description("用户密码"),
                    parameterWithName("optional").description("可选参数 - 测试使用").optional()
                ),
                relaxedResponseFields(
                    fieldWithPath("payload.accessToken").description("访问令牌"),
                    fieldWithPath("payload.refreshToken").description("刷新令牌")
                )
            )
        );
  }

  @Test
  public void isTokenCreatedErrorParameter() throws Exception {
    String url = this.authorizationBaseUrl + "?username=demo";

    this.mockMvc
        .perform(RestDocumentationRequestBuilders.post(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void isTokenCreatedErrorPassword() throws Exception {
    String url = this.authorizationBaseUrl + "?username=demo&password=122222";

    this.mockMvc
        .perform(RestDocumentationRequestBuilders.post(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void isTokenCreatedErrorUsername() throws Exception {
    String url = this.authorizationBaseUrl + "?username=unknown&password=233333";

    this.mockMvc
        .perform(RestDocumentationRequestBuilders.post(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void isTokenRefreshed() throws Exception {
    AuthorizedJwts token = this.getAuthorization("demo", "233333");
    this.mockMvc.perform(
        RestDocumentationRequestBuilders
            .post(String.format("%s/refresh?refreshToken=%s", this.authorizationBaseUrl, token.getRefreshToken()))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(token.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(jsonPath("payload.accessToken", is(notNullValue())))
        .andExpect(jsonPath("payload.refreshToken", is(notNullValue())))
        .andDo(
            this.restDocumentationResultHandler.document(
                requestParameters(
                    parameterWithName("refreshToken").description("刷新令牌")
                ),
                relaxedResponseFields(
                    fieldWithPath("payload.accessToken").description("访问令牌"),
                    fieldWithPath("payload.refreshToken").description("刷新令牌")
                )
            )
        )
        .andExpect(status().isOk());
  }

  @Test
  public void isTokenRevoked() throws Exception {
    AuthorizedJwts token = this.getAuthorization("demo", "233333");
    this.mockMvc.perform(
        RestDocumentationRequestBuilders
            .delete(String.format("%s/revoke", this.authorizationBaseUrl))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(token.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());
  }

  @Test
  public void isTokenRevokedConfirm() throws Exception {
    AuthorizedJwts token = this.getAuthorization("demo", "233333");
    this.mockMvc.perform(
        MockMvcRequestBuilders
            .delete(String.format("%s/revoke", this.authorizationBaseUrl))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(token.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());

    this.mockMvc.perform(
        MockMvcRequestBuilders
            .delete(String.format("%s/revoke", this.authorizationBaseUrl))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(token.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isUnauthorized());
  }
}

package ink.o.w.o.api;

import ink.o.w.o.server.domain.AuthorizedJwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    String url = authorizationBaseUrl + "?username=demo&password=233333";

    mockMvc
        .perform(RestDocumentationRequestBuilders
            .post(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("_links.refresh", is(notNullValue())))
        .andExpect(jsonPath("_links.revoke", is(notNullValue())))
        .andDo(
            this.restDocumentationResultHandler.document(
                requestParameters(
                    parameterWithName("username").description("用户名"),
                    parameterWithName("password").description("用户密码"),
                    parameterWithName("optional").description("可选参数 - 测试使用").optional()
                ),
                responseFields(
                    fieldWithPath("accessToken").description("访问令牌"),
                    fieldWithPath("refreshToken").description("刷新令牌"),
                    fieldWithPath("_links.refresh").description("刷新令牌的连接"),
                    fieldWithPath("_links.revoke").description("注销令牌的连接"),
                    subsectionWithPath("_links").ignored()
                )
            )
        );
  }

  @Test
  public void isTokenCreatedErrorParameter() throws Exception {
    String url = authorizationBaseUrl + "?username=demo";

    mockMvc
        .perform(RestDocumentationRequestBuilders.post(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void isTokenCreatedErrorPassword() throws Exception {
    String url = authorizationBaseUrl + "?username=demo&password=122222";

    mockMvc
        .perform(RestDocumentationRequestBuilders.post(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void isTokenCreatedErrorUsername() throws Exception {
    String url = authorizationBaseUrl + "?username=unknown&password=233333";

    mockMvc
        .perform(RestDocumentationRequestBuilders.post(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void isTokenRefreshed() throws Exception {
    AuthorizedJwts token = getAuthorization("demo", "233333");
    mockMvc.perform(
        RestDocumentationRequestBuilders
            .post(String.format("%s/refresh?refreshToken=%s", authorizationBaseUrl, token.getRefreshToken()))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(token.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(jsonPath("_links.revoke", is(notNullValue())))
        .andDo(
            this.restDocumentationResultHandler.document(
                requestParameters(
                    parameterWithName("refreshToken").description("刷新令牌")
                ),
                responseFields(
                    fieldWithPath("accessToken").description("访问令牌"),
                    fieldWithPath("refreshToken").description("刷新令牌"),
                    fieldWithPath("_links.revoke").description("注销令牌的连接"),
                    subsectionWithPath("_links").ignored()
                )
            )
        )
        .andExpect(status().isOk());
  }

  @Test
  public void isTokenRevoked() throws Exception {
    AuthorizedJwts token = getAuthorization("demo", "233333");
    mockMvc.perform(
        RestDocumentationRequestBuilders
            .delete(String.format("%s/revoke", authorizationBaseUrl))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(token.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());
  }

  @Test
  public void isTokenRevokedConfirm() throws Exception {
    AuthorizedJwts token = getAuthorization("demo", "233333");
    mockMvc.perform(
        MockMvcRequestBuilders
            .delete(String.format("%s/revoke", authorizationBaseUrl))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(token.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());

    mockMvc.perform(
        MockMvcRequestBuilders
            .delete(String.format("%s/revoke", authorizationBaseUrl))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(token.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isUnauthorized());
  }
}

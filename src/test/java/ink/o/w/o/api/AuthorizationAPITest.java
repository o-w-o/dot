package ink.o.w.o.api;

import ink.o.w.o.server.constant.HttpConstant;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.AuthorizedJwts;
import ink.o.w.o.server.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/3 上午9:52
 */

@Slf4j
public class AuthorizationAPITest extends APITest {

  private final String authorizationBaseUrl = HttpConstant.API_ENTRY + "/auth";
  @Autowired
  private AuthorizationService authorizationService;
  @Value("${spring.data.rest.base-path}")
  private String restDataBaseUrl;
  @Value("${management.endpoints.web.base-path}")
  private String endpointBaseUrl;

  @Test
  public void isTokenCreated() throws Exception {
    String url = authorizationBaseUrl + "/token?username=demo&password=233333";

    mockMvc
        .perform(RestDocumentationRequestBuilders.get(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(
            this.restDocumentationResultHandler.document(
                requestParameters(
                    parameterWithName("username").description("用户名"),
                    parameterWithName("password").description("用户密码"),
                    parameterWithName("optional").description("可选参数 - 测试使用").optional()
                ),
                responseFields(
                    fieldWithPath("data.accessToken").description("访问令牌"),
                    fieldWithPath("data.refreshToken").description("刷新令牌"),
                    fieldWithPath("statusCode").description("响应状态码"),
                    fieldWithPath("resultCode").description("服务状态码"),
                    fieldWithPath("result").description("服务是否正常"),
                    fieldWithPath("message").description("-").optional(),
                    fieldWithPath("path").description("-").optional(),
                    fieldWithPath("timestamp").description("-")
                )
            )
        )
        .andExpect(status().isOk())
        .andReturn().getResponse();
  }

  @Test
  public void isTokenCreatedErrorParameter() throws Exception {
    String url = authorizationBaseUrl + "/token?username=demo";

    mockMvc
        .perform(RestDocumentationRequestBuilders.get(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError())
        .andReturn().getResponse();
  }

  @Test
  public void isTokenCreatedErrorPassword() throws Exception {
    String url = authorizationBaseUrl + "/token?username=demo&password=122222";

    mockMvc
        .perform(RestDocumentationRequestBuilders.get(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError())
        .andReturn().getResponse();
  }

  @Test
  public void isTokenCreatedErrorUsername() throws Exception {
    String url = authorizationBaseUrl + "/token?username=unknown&password=233333";

    mockMvc
        .perform(RestDocumentationRequestBuilders.get(url)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError())
        .andReturn().getResponse();
  }

  @Test
  public void isTokenRefreshed() throws Exception {
    AuthorizedJwts token = authorizationService.authorize("demo", "233333").guard();
    mockMvc.perform(
        MockMvcRequestBuilders
            .post(String.format("%s/token?refreshToken=%s", authorizationBaseUrl, token.getRefreshToken()))
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON)
    )
        .andDo(
            this.restDocumentationResultHandler.document(
                requestParameters(
                    parameterWithName("refreshToken").description("刷新令牌")
                ),
                responseFields(
                    fieldWithPath("data").description("新的 accessToken 访问令牌"),
                    fieldWithPath("statusCode").description("响应状态码"),
                    fieldWithPath("resultCode").description("服务状态码"),
                    fieldWithPath("result").description("服务是否正常"),
                    fieldWithPath("message").description("-").optional(),
                    fieldWithPath("path").description("-").optional(),
                    fieldWithPath("timestamp").description("-")
                )
            )
        )
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();
  }

  @Test
  public void isRestDataUnauthorized() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get(restDataBaseUrl + "/samples")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  public void isRestDataForbidden() throws Exception {
    AuthorizedJwts token = authorizationService.authorize("demo", "233333").guard();

    mockMvc.perform(MockMvcRequestBuilders
        .get(restDataBaseUrl + "/samples")
        .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  public void isRestDataOk() throws Exception {
    AuthorizedJwts token = authorizationService.authorize("sample", "233333").guard();

    mockMvc.perform(MockMvcRequestBuilders
        .get(restDataBaseUrl + "/samples")
        .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void isEndpointUnauthorized() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get(endpointBaseUrl + "/health")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  public void isEndpointForbidden() throws Exception {
    AuthorizedJwts token = authorizationService.authorize("demo", "233333").guard();

    mockMvc.perform(MockMvcRequestBuilders
        .get(endpointBaseUrl + "/health")
        .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  public void isEndpointOk() throws Exception {
    AuthorizedJwts token = authorizationService.authorize("actuator", "233333").guard();

    mockMvc.perform(MockMvcRequestBuilders
        .get(endpointBaseUrl + "/health")
        .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }
}

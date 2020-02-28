package ink.o.w.o.api;

import ink.o.w.o.resource.authorization.domain.AuthorizedJwt;
import ink.o.w.o.resource.authorization.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class SampleAPITest extends APITest {
  @Autowired
  private AuthorizationService authorizationService;

  @Test
  public void sampleAuthenticationHeader() throws Exception {
    mockMvc
        .perform(
            get("/users/5")
                .header(
                    getAuthorizationHeaderKey(),
                    getAuthorizationHeaderValue(
                        authorizationService.authorize("qa", "233333").guard().getAccessToken()
                    )
                )
            .header("Content-Type", "application/json")
        )
        .andExpect(status().isOk())
        .andDo(this.restDocumentationResultHandler.document(
            requestHeaders(
                headerWithName(AuthorizedJwt.AUTHORIZATION_HEADER_KEY).description(String.format("认证字段，Bearer 格式： `%s + jwt`", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX)).optional(),
                headerWithName("Content-Type").description("载荷格式，默认 `application/json`")
            ),
            responseHeaders(
                headerWithName("Content-Type").description("载荷格式，默认 `application/json`")
            )
        ))
        .andReturn().getResponse();
  }

  @Test
  public void sampleAuthenticationHeader401() throws Exception {
    mockMvc
        .perform(
            get("/users/5")
        )
        .andExpect(status().isUnauthorized())
        .andReturn().getResponse();
  }

  @Test
  public void sampleAuthenticationHeader403() throws Exception {
    mockMvc
        .perform(
            get("/users/5")
                .header(
                    getAuthorizationHeaderKey(),
                    getAuthorizationHeaderValue(
                        authorizationService.authorize("demo", "233333").guard().getAccessToken()
                    )
                )
        )
        .andExpect(status().isForbidden())
        .andDo(

            this.restDocumentationResultHandler.document(
                responseFields(
                    fieldWithPath("code").description("服务状态码"),
                    fieldWithPath("payload").description("附加载荷"),
                    fieldWithPath("message").description("-").optional(),
                    fieldWithPath("method").description("-").optional(),
                    fieldWithPath("path").description("-").optional(),
                    fieldWithPath("timestamp").description("-")
                )
            )
        )
        .andReturn().getResponse();
  }
}

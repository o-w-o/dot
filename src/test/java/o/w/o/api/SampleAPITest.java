package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.domain.AuthorizedJwt;
import o.w.o.resource.system.authorization.service.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class SampleAPITest extends APITest {
  @Autowired
  private AuthorizationService authorizationService;

  @Test
  public void sampleAuthenticationHeader() throws Exception {
    this.mockMvc
        .perform(
            get("/users/5")
                .header(
                    getAuthorizationHeaderKey(),
                    getAuthorizationHeaderValue(
                        this.authorizationService.authorize("qa", "233333").guard().getAccessToken()
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
    this.mockMvc
        .perform(
            get("/users/5")
        )
        .andExpect(status().isUnauthorized())
        .andReturn().getResponse();
  }

  @Test
  public void sampleAuthenticationHeader403() throws Exception {
    this.mockMvc
        .perform(
            get("/users/5")
                .header(
                    getAuthorizationHeaderKey(),
                    getAuthorizationHeaderValue(
                        this.authorizationService.authorize("demo", "233333").guard().getAccessToken()
                    )
                )
        )
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("message").isNotEmpty())
        .andDo(

            this.restDocumentationResultHandler.document(
                relaxedResponseFields(
                    fieldWithPath("code").description("服务状态码"),
                    fieldWithPath("message").description("-")
                )
            )
        )
        .andReturn().getResponse();
  }
}

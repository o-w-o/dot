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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/3 上午9:52
 */

@Slf4j
class AuthorizationAPITest extends APITest {

    @Autowired
    private AuthorizationService authorizationService;

    private final String authorizationBaseUrl = HttpConstant.API_ENTRY + "/auth";

    @Test
    void createToken() throws Exception {
        String url = authorizationBaseUrl + "/token?username=demo&password=233333";

        String responseDataJson = mockMvc
            .perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON))

            .andDo(document("auth--get-access_token"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
    }

    @Test
    void refreshToken() throws Exception {
        AuthorizedJwts token = authorizationService.authorize("demo", "233333").guard();
        mockMvc.perform(MockMvcRequestBuilders
            .post(authorizationBaseUrl + "/token?refreshToken=" + token.getRefreshToken())
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-refresh_token"))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Value("${spring.data.rest.base-path}")
    private String restDataBaseUrl;

    @Test
    void isRestDataUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get(restDataBaseUrl + "/samples")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-es-unauthorized"))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void isRestDataForbidden() throws Exception {
        AuthorizedJwts token = authorizationService.authorize("demo", "233333").guard();

        mockMvc.perform(MockMvcRequestBuilders
            .get(restDataBaseUrl + "/samples")
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-es-forbidden"))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    void isRestDataOk() throws Exception {
        AuthorizedJwts token = authorizationService.authorize("sample", "233333").guard();

        mockMvc.perform(MockMvcRequestBuilders
            .get(restDataBaseUrl + "/samples")
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-es-ok"))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Value("${management.endpoints.web.base-path}")
    private String endpointBaseUrl;

    @Test
    void isEndpointUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get(endpointBaseUrl + "/env")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-endpoint-unauthorized"))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void isEndpointForbidden() throws Exception {
        AuthorizedJwts token = authorizationService.authorize("demo", "233333").guard();

        mockMvc.perform(MockMvcRequestBuilders
            .get(endpointBaseUrl + "/env")
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-endpoint-forbidden"))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    void isEndpointOk() throws Exception {
        AuthorizedJwts token = authorizationService.authorize("actuator", "233333").guard();

        mockMvc.perform(MockMvcRequestBuilders
            .get(endpointBaseUrl + "/env")
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_HEADER_VAL_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-endpoint-ok"))
            .andExpect(status().isOk())
            .andReturn();
    }
}

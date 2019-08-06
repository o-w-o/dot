package ink.o.w.o.api;

import com.alibaba.fastjson.JSON;
import ink.o.w.o.config.constant.HttpConstant;
import ink.o.w.o.config.domain.HttpResponseData;
import ink.o.w.o.server.auth.domain.AuthorizedJwt;
import ink.o.w.o.server.auth.domain.AuthorizedToken;
import ink.o.w.o.server.auth.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/3 上午9:52
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
class AuthorizationAPITest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("auth");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorizationService authorizationService;

    private AuthorizedToken token;

    @BeforeEach
    void initToken() {
        token = authorizationService.authorize("demo", "233333");
        logger.info("initToken", token);
    }

    private final String baseUrl = HttpConstant.API_ENTRY + "/auth";

    @Test
    void createToken() throws Exception {
        String url = baseUrl + "/token?username=demo&password=233333";

        String responseDataJson = mvc
            .perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON))

            .andDo(document("auth--get-access_token"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        AuthorizedToken authorizedToken = JSON.parseObject(
            JSON.parseObject(
                responseDataJson,
                HttpResponseData.class
            ).getData().toString(),
            AuthorizedToken.class
        );
        logger.info(authorizedToken.getAccessToken());
    }

    @Test
    void refreshToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders
            .post(baseUrl + "/token?refreshToken=" + token.getRefreshToken())
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-refresh_token"))
            .andExpect(status().isOk())
            .andReturn();
    }
}

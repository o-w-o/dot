package ink.o.w.o.api;

import ink.o.w.o.Application;
import ink.o.w.o.config.constant.HttpConstant;
import ink.o.w.o.server.auth.domain.AuthorizedJwt;
import ink.o.w.o.server.auth.domain.AuthorizedToken;
import ink.o.w.o.server.auth.service.AuthorizationService;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/3 上午9:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RESTDataAPITest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("rest-data");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorizationService authorizationService;

    private final String baseUrl = HttpConstant.API_ENTRY + "/samples";

    @Test
    void isUnauthorized() throws Exception {
        AuthorizedToken token = authorizationService.authorize("demo", "233333");

        mvc.perform(MockMvcRequestBuilders
            .get(baseUrl)
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-es-unauthorized"))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    void isOk() throws Exception {
        AuthorizedToken token = authorizationService.authorize("sample", "233333");

        mvc.perform(MockMvcRequestBuilders
            .get(baseUrl)
            .header("Authorization", AuthorizedJwt.AUTHORIZATION_PREFIX + token.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(document("auth--get-es-ok"))
            .andExpect(status().isOk())
            .andReturn();
    }
}

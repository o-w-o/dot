package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.domain.AuthorizedJwt;
import o.w.o.resource.system.authorization.domain.property.AuthorizationHeader;
import o.w.o.resource.system.role.domain.Role;
import o.w.o.resource.system.role.util.RoleUtil;
import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.service.UserService;
import o.w.o.server.helper.JsonHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

/**
 * APITest
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/8 上午8:59
 */

@Slf4j
@AutoConfigureMockMvc
@AutoConfigureCache
@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {
  public static final String QA_NAME = "qa";
  public static final String QA_PASSWORD = "233333";
  protected static AuthorizedJwt qaJwt;
  protected static User qa;
  protected final String authorizationBaseUrl = "/authorization";
  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected JsonHelper jsonHelper;

  @Autowired
  protected UserService userService;

  protected static String getAuthorizationHeaderKey() {
    return AuthorizationHeader.HEADER_KEY;
  }

  protected static String getAuthorizationHeaderValue(String accessToken) {
    return AuthorizationHeader.HEADER_VAL_PREFIX + accessToken;
  }

  protected static void destroyQaJwt() throws Exception {
    qaJwt = null;
  }

  protected static void destroyQaUser() {
    qa = null;
  }

  @AfterAll
  public static void afterAll(@Autowired JsonHelper jsonHelper) throws Exception {
    destroyQaJwt();
    destroyQaUser();
  }

  synchronized protected void createQaJwt() throws Exception {
    if (Objects.isNull(qaJwt)) {
      String url = String.format("%s?username=%s&password=%s", this.authorizationBaseUrl, QA_NAME, QA_PASSWORD);

      var result = this.mockMvc
          .perform(
              MockMvcRequestBuilders.post(url)
          )
          .andReturn()
          .getResponse()
          .getContentAsString();

      qaJwt = jsonHelper.readApiResultPayload(result, AuthorizedJwt.class).orElseThrow();
    }
  }

  synchronized protected void createQaUser() {
    if (Objects.isNull(qa)) {
      var srvUser = userService.getUserByUsername(QA_NAME);

      if (srvUser.isSuccess()) {
        qa = srvUser.getPayload();
        return;
      }

      qa = userService
          .register(
              new User()
                  .setName(QA_NAME)
                  .setPassword(QA_PASSWORD)
                  .setRoles(RoleUtil.of(Role.USER, Role.MASTER))
          )
          .guard();
    }
  }

  @BeforeEach
  synchronized public void beforeEach() throws Exception {
    this.createQaUser();
    this.createQaJwt();
  }
}

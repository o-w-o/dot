package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authorization.domain.AuthorizedJwt;
import o.w.o.resource.system.authorization.domain.property.AuthorizationHeader;
import o.w.o.resource.system.role.domain.Role;
import o.w.o.resource.system.role.util.RoleUtil;
import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.service.UserService;
import o.w.o.server.helper.JsonHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

  protected final String authorizationBaseUrl = "/authorization";

  protected AuthorizedJwt qaJwt;
  protected User qa;


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

  protected AuthorizedJwt createQaJwt() throws Exception {
    String url = String.format("%s?username=%s&password=%s", this.authorizationBaseUrl, QA_NAME, QA_PASSWORD);

    var result = this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(url)
        )
        .andReturn()
        .getResponse()
        .getContentAsString();

    return jsonHelper.readServiceResultPayload(result, AuthorizedJwt.class).orElseThrow();
  }

  protected User createQaUser() {
    var srvUser = userService.getUserByUsername(QA_NAME);
    if (srvUser.isSuccess()) {
      return srvUser.getPayload();
    }

    return userService
        .register(
            new User()
                .setName(QA_NAME)
                .setPassword(QA_PASSWORD)
                .setRoles(RoleUtil.of(Role.USER, Role.MASTER))
        )
        .guard();

  }

  protected void destroyQaUser() {
    var srvUser = userService.getUserByUsername(QA_NAME);
    if (srvUser.isSuccess()) {
      userService
          .revoke(srvUser.getPayload().getId())
          .guard();
    }
  }

  @BeforeEach
  synchronized public void beforeEach() throws Exception {
    this.qa = this.createQaUser();
    this.qaJwt = this.createQaJwt();
  }

  @AfterEach
  synchronized public void afterEach() {
    this.destroyQaUser();
  }
}

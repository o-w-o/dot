package o.w.o.api;

import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.user.constant.UserGender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class MyApiTest extends ApiTest {
  private final String myBaseUrl = "/api/my";

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void getOneUserProfile() throws Exception {
    this.mockMvc.perform(
        MockMvcRequestBuilders
            .get(this.myBaseUrl + "/profile")
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(qaJwt.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());
  }

  @Test
  void modifyOneUserProfile() throws Exception {
    this.mockMvc.perform(
        MockMvcRequestBuilders
            .patch(this.myBaseUrl + "/profile")
            .content(
                jsonHelper.toJsonString(qa.setGender(UserGender.BOY))
            )
            .contentType(MediaType.APPLICATION_JSON)
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(qaJwt.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(jsonPath("payload.gender").value(UserGender.BOY.name()))
        .andExpect(status().isOk());
  }

  @Test
  void modifyOneUserPassword() throws Exception {
    this.mockMvc.perform(
        MockMvcRequestBuilders
            .patch(String.format("%s/password?prevPassword=%s&password=%s", this.myBaseUrl, QA_PASSWORD, QA_PASSWORD.hashCode()))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(qaJwt.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());

    Assertions.assertTrue(passwordEncoder.matches(QA_PASSWORD.hashCode() + "", userService.getUserById(qa.getId()).guard().getPassword()));


    this.mockMvc.perform(
        MockMvcRequestBuilders
            .patch(String.format("%s/password?prevPassword=%s&password=%s", this.myBaseUrl, QA_PASSWORD.hashCode(), QA_PASSWORD))
            .header(getAuthorizationHeaderKey(), getAuthorizationHeaderValue(qaJwt.getAccessToken()))
            .accept(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isOk());

    Assertions.assertTrue(passwordEncoder.matches(QA_PASSWORD + "", userService.getUserById(qa.getId()).guard().getPassword()));

  }
}

package ink.o.w.o.resource.integration.aliyun.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.integration.aliyun.domain.Policy;
import ink.o.w.o.resource.system.authorization.domain.AuthorizedUser;
import ink.o.w.o.server.io.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StsHelper {
  private final JsonHelper jsonHelper;

  @Autowired
  public StsHelper(JsonHelper jsonHelper) {
    this.jsonHelper = jsonHelper;
  }

  public String generateStsSessionName(AuthorizedUser authorizedUser) {
    return String.format(
        "%s@%s",
        authorizedUser.getUsername(),
        authorizedUser.getIp().replace(":", "-")
    );
  }

  public String generateStsPolicy(Policy policy) {
    var json = "{}";
    try {
      json = jsonHelper.toJsonString(policy);
    } catch (JsonProcessingException e) {
      logger.warn("JsonProcessingException -> {}", e.getMessage());
    }
    return json;
  }
}

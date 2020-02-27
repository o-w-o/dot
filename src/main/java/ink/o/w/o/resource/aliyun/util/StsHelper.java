package ink.o.w.o.resource.aliyun.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.aliyun.domain.Policy;
import ink.o.w.o.resource.authorization.domain.AuthorizedUser;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StsHelper {
  private final JSONHelper jsonHelper;

  @Autowired
  public StsHelper(JSONHelper jsonHelper) {
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
      json = jsonHelper.toJSONString(policy);
    } catch (JsonProcessingException e) {
      logger.warn("JsonProcessingException -> {}", e.getMessage());
    }
    return json;
  }
}

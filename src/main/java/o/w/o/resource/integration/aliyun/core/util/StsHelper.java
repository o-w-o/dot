package o.w.o.resource.integration.aliyun.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import o.w.o.resource.integration.aliyun.core.domain.Policy;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import o.w.o.server.io.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class StsHelper {

  @Resource
  private JsonHelper jsonHelper;

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

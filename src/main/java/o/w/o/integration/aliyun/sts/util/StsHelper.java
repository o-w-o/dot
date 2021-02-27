package o.w.o.integration.aliyun.sts.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import o.w.o.integration.aliyun.sts.domain.Policy;
import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.infrastructure.helper.JsonHelper;
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

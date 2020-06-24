package o.w.o.resource.system.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.ResourceServiceTest;
import o.w.o.resource.system.user.service.UserService;
import o.w.o.server.io.json.JsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;

@Slf4j
@AutoConfigureDataRedis
public class UserServiceTest extends ResourceServiceTest {
  @Autowired
  private UserService userService;

  @Autowired
  private JsonHelper jsonHelper;

  @Test
  public void testCacheables() throws JsonProcessingException {
    logger.info("1 -> {}", this.jsonHelper.toJsonString(this.userService.getUserById(1)));
    logger.info("2 -> {}", this.jsonHelper.toJsonString(this.userService.getUserById(1)));
    Assertions.assertEquals(this.userService.getUserById(1).guard().getId(), 1);
  }
}

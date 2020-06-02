package ink.o.w.o.resource.system.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.system.user.service.UserService;
import ink.o.w.o.server.io.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@AutoConfigureCache
@AutoConfigureDataRedis
@AutoConfigureDataJpa
@SpringBootTest
public class UserServiceTest {
  @Autowired
  private UserService userService;

  @Autowired
  private JsonHelper jsonHelper;

  @Test
  public void testCacheables() throws JsonProcessingException {
    logger.info("1 -> {}", jsonHelper.toJsonString(userService.getUserById(1)));
    logger.info("2 -> {}", jsonHelper.toJsonString(userService.getUserById(1)));
    Assertions.assertEquals(userService.getUserById(1).guard().getId(), 1);
  }
}

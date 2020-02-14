package ink.o.w.o.resource.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@AutoConfigureCache
@AutoConfigureDataRedis
@AutoConfigureDataJpa
@SpringBootTest
public class UserServiceTest {
  @Resource
  private UserService userService;

  @Resource
  private JSONHelper jsonHelper;

  @Test
  public void testCacheables() throws JsonProcessingException {
    logger.info("1 -> {}", jsonHelper.toJSONString(userService.getUserById(1)));
    logger.info("2 -> {}", jsonHelper.toJSONString(userService.getUserById(1)));
    Assertions.assertEquals(userService.getUserById(1).guard().getId(), 1);
  }
}

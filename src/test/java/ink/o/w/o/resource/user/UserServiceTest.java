package ink.o.w.o.resource.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
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
  UserService userService;

  @Resource
  JSONHelper jsonHelper;

  @Test
  public void testCacheables() throws JsonProcessingException {
    logger.info("1 -> {}", jsonHelper.toJSONString(userService.getUserById(1)));
    logger.info("2 -> {}", jsonHelper.toJSONString(userService.getUserById(1)));
  }
}

package o.w.o.resource.system.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.ResourceServiceTest;
import o.w.o.resource.system.role.domain.Role;
import o.w.o.resource.system.role.util.RoleUtil;
import o.w.o.resource.system.user.domain.User;
import o.w.o.server.helper.JsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

  private User cacheUser;
  private User cacheUser2;
  private User cacheUser3;

  @BeforeEach
  public void createUser() {
    if (cacheUser == null) {
      cacheUser = userService
          .register(
              new User()
                  .setName("cache")
                  .setRoles(RoleUtil.of(Role.USER))
                  .setPassword("233333")
          )
          .guard();
      cacheUser2 = userService
          .register(
              new User()
                  .setName("cache2")
                  .setRoles(RoleUtil.of(Role.USER))
                  .setPassword("233333")
          )
          .guard();
      cacheUser3 = userService
          .register(
              new User()
                  .setName("cache3")
                  .setRoles(RoleUtil.of(Role.USER))
                  .setPassword("233333")
          )
          .guard();
    }
  }

  @Test
  public void testCacheables() throws JsonProcessingException {
    var uid = cacheUser.getId();

    var u1 = this.userService.getUserById(uid).guard();
    logger.info("1 -> {}", this.jsonHelper.toJsonString(u1));
    var u2 = this.userService.getUserById(cacheUser2.getId());
    logger.info("2 -> {}", this.jsonHelper.toJsonString(u2));
    var u3 = this.userService.getUserById(cacheUser3.getId());
    logger.info("3 -> {}", this.jsonHelper.toJsonString(u3));
    var u11 = this.userService.getUserById(uid);
    logger.info("4 -> {}", this.jsonHelper.toJsonString(u11));
    Assertions.assertEquals(u1.getId(), uid);
  }
}

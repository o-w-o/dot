package o.w.o.domain.core.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.ResourceServiceTest;
import o.w.o.domain.core.role.domain.Role;
import o.w.o.domain.core.role.util.RoleUtil;
import o.w.o.domain.core.user.domain.User;
import o.w.o.infrastructure.helper.JsonHelper;
import org.junit.jupiter.api.AfterEach;
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
  public void createUsers() {
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

  @AfterEach
  public void destroyUsers() {
    userService.revoke(cacheUser.getId());
    userService.revoke(cacheUser2.getId());
    userService.revoke(cacheUser3.getId());
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

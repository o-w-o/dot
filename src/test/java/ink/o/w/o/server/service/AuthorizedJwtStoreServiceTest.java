package ink.o.w.o.server.service;

import ink.o.w.o.resource.user.domain.User;
import ink.o.w.o.resource.user.service.UserService;
import ink.o.w.o.server.domain.AuthorizedJwt;
import ink.o.w.o.server.domain.AuthorizedJwtStore;
import ink.o.w.o.server.repository.AuthorizedJwtStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@AutoConfigureDataJpa
@AutoConfigureDataRedis
@SpringBootTest
class AuthorizedJwtStoreServiceTest {
  @Autowired
  private AuthorizedJwtStoreRepository authorizedJwtStoreRepository;

  @Autowired
  private AuthorizedJwtStoreService authorizedJwtStoreService;

  @Autowired
  private UserService userService;

  private User user;

  @BeforeEach
  public void setUp() {
    user = userService.getUserByUsername("qa").guard();
  }

  @AfterEach
  public void setDown() {
    authorizedJwtStoreRepository.deleteAll();
  }

  @Test
  public void register() {
    var result = authorizedJwtStoreService.register(user);

    assertTrue(result.getSuccess());
    assertNotNull(result.getPayload());
  }

  @Test
  public void revoke() {
    var jwtString = authorizedJwtStoreService.register(user).guard().getAccessToken();

    var jwt = AuthorizedJwt.generateJwtFromJwtString(jwtString);

    authorizedJwtStoreService.revoke(jwt);

    assertTrue(authorizedJwtStoreRepository.findById(AuthorizedJwtStore.generateUuid(jwt)).isEmpty());
  }

  @Test
  public void revokeAll() {
    authorizedJwtStoreService.register(user);
    authorizedJwtStoreService.register(user);
    var jwtString = authorizedJwtStoreService.register(user).guard().getAccessToken();

    logger.info("count -> {}", authorizedJwtStoreRepository.findByUserId(user.getId()).size());
    assertEquals(3, authorizedJwtStoreRepository.findByUserId(user.getId()).size());

    authorizedJwtStoreService.revokeAll(user.getId());

    assertEquals(0, authorizedJwtStoreRepository.findByUserId(user.getId()).size());
  }

  @Test
  public void reset() {
    authorizedJwtStoreService.reset();
    assertEquals(authorizedJwtStoreRepository.count(), 0);
  }

  @Test
  public void refresh() {
  }

  @Test
  public void validate() {
  }
}
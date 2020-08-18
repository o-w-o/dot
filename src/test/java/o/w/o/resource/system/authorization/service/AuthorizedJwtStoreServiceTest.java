package o.w.o.resource.system.authorization.service;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.ResourceServiceTest;
import o.w.o.resource.system.authorization.domain.AuthorizedJwt;
import o.w.o.resource.system.authorization.domain.AuthorizedJwtStore;
import o.w.o.resource.system.authorization.repository.AuthorizedJwtStoreRepository;
import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AuthorizedJwtStoreServiceTest extends ResourceServiceTest {
  @Autowired
  private AuthorizedJwtStoreRepository authorizedJwtStoreRepository;

  @Autowired
  private AuthorizedJwtStoreService authorizedJwtStoreService;

  @Autowired
  private UserService userService;

  private User user;

  @BeforeEach
  public void setUp() {
    this.user = this.userService.getUserByUsername("qa").guard();
  }

  @AfterEach
  public void setDown() {
    this.authorizedJwtStoreRepository.deleteAll();
  }

  @Test
  public void register() {
    var result = this.authorizedJwtStoreService.register(this.user);

    assertTrue(result.getSuccess());
    assertNotNull(result.getPayload());
  }

  @Test
  public void revoke() {
    var jwtString = this.authorizedJwtStoreService.register(this.user).guard().getAccessToken();

    var jwt = AuthorizedJwt.generateJwtFromJwtString(jwtString);

    this.authorizedJwtStoreService.revoke(jwt);

    assertTrue(this.authorizedJwtStoreRepository.findById(AuthorizedJwtStore.generateUuid(jwt)).isEmpty());
  }

  @Test
  public void revokeAll() {
    this.authorizedJwtStoreService.register(this.user);
    this.authorizedJwtStoreService.register(this.user);
    logger.info("count -> {}", this.authorizedJwtStoreRepository.findByUserId(this.user.getId()).size());
    assertEquals(2, this.authorizedJwtStoreRepository.findByUserId(this.user.getId()).size());

    this.authorizedJwtStoreService.revokeAll(this.user.getId());

    assertEquals(0, this.authorizedJwtStoreRepository.findByUserId(this.user.getId()).size());
  }

  @Test
  public void reset() {
    this.authorizedJwtStoreService.reset();
    assertEquals(this.authorizedJwtStoreRepository.count(), 0);
  }

  @Test
  public void refresh() {
  }

  @Test
  public void validate() {
  }
}
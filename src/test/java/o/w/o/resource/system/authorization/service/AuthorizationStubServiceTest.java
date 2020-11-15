package o.w.o.resource.system.authorization.service;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.ResourceServiceTest;
import o.w.o.resource.system.authorization.domain.AuthorizationStub;
import o.w.o.resource.system.authorization.domain.AuthorizationJwt;
import o.w.o.resource.system.authorization.repository.AuthorizationStubRepository;
import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AuthorizationStubServiceTest extends ResourceServiceTest {
  @Autowired
  private AuthorizationStubRepository authorizationStubRepository;

  @Autowired
  private AuthorizationStubService authorizationStubService;

  @Autowired
  private UserService userService;

  private User user;

  @BeforeEach
  public void setUp() {
    this.user = this.userService.getUserByUsername("qa").guard();
  }

  @AfterEach
  public void setDown() {
    this.authorizationStubRepository.deleteAll();
  }

  @Test
  public void register() {
    var result = this.authorizationStubService.register(this.user);

    assertTrue(result.isSuccess());
    assertNotNull(result.getPayload());
  }

  @Test
  public void revoke() {
    var jwtString = this.authorizationStubService.register(this.user).guard().getAccessToken();

    var jwt = AuthorizationJwt.fork(jwtString);

    this.authorizationStubService.revoke(AuthorizationStub.generateId(jwt));

    assertTrue(this.authorizationStubRepository.findById(AuthorizationStub.generateId(jwt)).isEmpty());
  }

  @Test
  public void revokeAll() {
    this.authorizationStubService.register(this.user);
    this.authorizationStubService.register(this.user);
    logger.info("count -> {}", this.authorizationStubRepository.findByUid(this.user.getId()).size());
    assertEquals(2, this.authorizationStubRepository.findByUid(this.user.getId()).size());

    this.authorizationStubService.revokeAll(this.user.getId());

    assertEquals(0, this.authorizationStubRepository.findByUid(this.user.getId()).size());
  }

  @Test
  public void reset() {
    this.authorizationStubService.reset();
    assertEquals(this.authorizationStubRepository.count(), 0);
  }

  @Test
  public void refresh() {
  }

  @Test
  public void validate() {
  }
}
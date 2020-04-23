package ink.o.w.o.resource.integration.unsplash.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UnsplashServiceTest {
  @Autowired
  private UnsplashService unsplashService;

  @Test
  public void retrieveOneRandomPhoto() {
    var result = unsplashService.retrieveOneRandomPhoto("fire");
    logger.info("result -> {}", result);
    assertNotNull(result);
  }

  @Test
  public void retrieveRandomPhotos() {
    var result = unsplashService.retrieveRandomPhotos("fire", 2);
    logger.info("result -> {}", result);
    assertNotNull(result);
  }

  @Test
  public void searchPhotos() {
    var result = unsplashService.searchPhotos("fire");
    logger.info("result -> {}", result);
    assertNotNull(result);
  }
}
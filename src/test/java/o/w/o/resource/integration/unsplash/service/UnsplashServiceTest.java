package o.w.o.resource.integration.unsplash.service;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.ResourceServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class UnsplashServiceTest extends ResourceServiceTest {
  @Autowired
  private UnsplashService unsplashService;

  @Test
  public void retrieveOneRandomPhoto() {
    var result = this.unsplashService.retrieveOneRandomPhoto("fire");
    logger.info("result -> {}", result);
    assertNotNull(result);
  }

  @Test
  public void retrieveRandomPhotos() {
    var result = this.unsplashService.retrieveRandomPhotos("fire", 2);
    logger.info("result -> {}", result);
    assertNotNull(result);
  }

  @Test
  public void searchPhotos() {
    var result = this.unsplashService.searchPhotos("fire");
    logger.info("result -> {}", result);
    assertNotNull(result);
  }
}
package o.w.o.resource.integration.unsplash.service;

public interface UnsplashService {
  Object retrieveOneRandomPhoto(String keyword);

  Object[] retrieveRandomPhotos(String keyword, Integer count);

  Object searchPhotos(String keyword);
}

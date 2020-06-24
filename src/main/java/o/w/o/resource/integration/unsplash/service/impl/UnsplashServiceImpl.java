package o.w.o.resource.integration.unsplash.service.impl;

import o.w.o.resource.integration.unsplash.constant.properties.MyUnsplashProperties;
import o.w.o.resource.integration.unsplash.request.RetrieveRandomPhotosRequest;
import o.w.o.resource.integration.unsplash.request.SearchPhotosRequest;
import o.w.o.resource.integration.unsplash.service.UnsplashService;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UnsplashServiceImpl implements UnsplashService {

  @Resource
  private MyUnsplashProperties myUnsplashProperties;
  @Resource
  private RetrieveRandomPhotosRequest retrieveRandomPhotosRequest;
  @Resource
  private SearchPhotosRequest searchPhotosRequest;

  @Override
  public Object retrieveOneRandomPhoto(String keyword) {

    return this.retrieveRandomPhotosRequest.sendRequest(
        new RetrieveRandomPhotosRequest.Parameters()
            .setQuery(keyword)
    );
  }

  @Override
  public Object[] retrieveRandomPhotos(String keyword, @Range(min = 2, max = 10, message = "1 < count < 10") Integer count) {

    return this.retrieveRandomPhotosRequest.sendRequest(
        new RetrieveRandomPhotosRequest.Parameters()
            .setQuery(keyword),
        count
    );
  }

  @Override
  public Object searchPhotos(String keyword) {

    return this.searchPhotosRequest.sendRequest(
        new SearchPhotosRequest.Parameters()
            .setQuery(keyword)
    );
  }
}

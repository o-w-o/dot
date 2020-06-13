package ink.o.w.o.resource.integration.unsplash.service.impl;

import ink.o.w.o.resource.integration.unsplash.constant.properties.MyUnsplashProperties;
import ink.o.w.o.resource.integration.unsplash.request.RetrieveRandomPhotosRequest;
import ink.o.w.o.resource.integration.unsplash.request.SearchPhotosRequest;
import ink.o.w.o.resource.integration.unsplash.service.UnsplashService;
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

    return retrieveRandomPhotosRequest.sendRequest(
        new RetrieveRandomPhotosRequest.Parameters()
            .setClientId(myUnsplashProperties.getAccessKeyId())
            .setQuery(keyword)
    );
  }

  @Override
  public Object[] retrieveRandomPhotos(String keyword, @Range(min = 2, max = 10, message = "1 < count < 10") Integer count) {

    return retrieveRandomPhotosRequest.sendRequest(
        new RetrieveRandomPhotosRequest.Parameters()
            .setClientId(myUnsplashProperties.getAccessKeyId())
            .setQuery(keyword),
        count
    );
  }

  @Override
  public Object searchPhotos(String keyword) {

    return searchPhotosRequest.sendRequest(
        new SearchPhotosRequest.Parameters()
            .setClientId(myUnsplashProperties.getAccessKeyId())
            .setQuery(keyword)
    );
  }
}

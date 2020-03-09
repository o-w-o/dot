package ink.o.w.o.integration.unsplash.service.impl;

import ink.o.w.o.integration.unsplash.constant.properties.MyUnsplashProperties;
import ink.o.w.o.integration.unsplash.request.RetrieveRandomPhotosRequest;
import ink.o.w.o.integration.unsplash.request.SearchPhotosRequest;
import ink.o.w.o.integration.unsplash.service.UnsplashService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnsplashServiceImpl implements UnsplashService {
  private final MyUnsplashProperties myUnsplashProperties;
  private final RetrieveRandomPhotosRequest retrieveRandomPhotosRequest;
  private final SearchPhotosRequest searchPhotosRequest;

  @Autowired
  public UnsplashServiceImpl(RetrieveRandomPhotosRequest retrieveRandomPhotosRequest, SearchPhotosRequest searchPhotosRequest, MyUnsplashProperties myUnsplashProperties) {
    this.retrieveRandomPhotosRequest = retrieveRandomPhotosRequest;
    this.searchPhotosRequest = searchPhotosRequest;
    this.myUnsplashProperties = myUnsplashProperties;
  }

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

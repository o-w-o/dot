package o.w.o.integration.unsplash.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import o.w.o.integration.unsplash.constant.UnsplashConstant;
import o.w.o.integration.unsplash.constant.properties.MyUnsplashProperties;
import o.w.o.util.RequestUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

@Slf4j
@Component
public class SearchPhotosRequest {
  private static final String REQUEST_URL = UnsplashConstant.URL.LOCATION + "/search/photos";

  @Resource
  private RestTemplate restTemplate;

  @Resource
  private MyUnsplashProperties myUnsplashProperties;

  public Result sendRequest(Parameters parameters) {
    return this.restTemplate
        .exchange(
            String.format("%s%s", REQUEST_URL, RequestUtil.stringifyQueryParameters(parameters)),
            HttpMethod.GET,
            UnsplashConstant.URL.getRequestHeader(this.myUnsplashProperties.getAccessKeyId()),
            Result.class
        )
        .getBody();
  }

  @Data
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  public static class Parameters {
    @JsonProperty("client_id")
    private String clientId;

    // Search terms.
    private String query;

    // Page number to retrieve. (Optional; default: 1)
    private String page;

    // Number of items per page. (Optional; default: 10)
    @JsonProperty("per_page")
    private Integer perPage = 3;

    // How to sort the photos. (Optional; default: relevant). Valid values are latest and relevant.
    @JsonProperty("order_by")
    private String orderBy;

    // Collection ID(‘s) to narrow search. If multiple, comma-separated.
    private String collections;

    // Filter results by color. Valid values are: black_and_white, black, white, yellow, orange, red, purple, magenta, green, teal, and blue.
    private String color;

    // Filter search results by photo orientation. Valid values are landscape, portrait, and squarish.
    private String orientation;
  }

  public static class Result extends HashMap<String, Object> {

    private static final long serialVersionUID = -7334184746353689518L;
  }
}

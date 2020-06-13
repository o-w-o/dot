package o.w.o.resource.integration.unsplash.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import o.w.o.resource.integration.unsplash.constant.UnsplashConstant;
import o.w.o.util.RequestHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
  private RequestHelper requestHelper;

  public Result sendRequest(Parameters parameters) {
    return this.restTemplate.getForObject(
        String.format("%s%s", REQUEST_URL, requestHelper.stringifyQueryParameters(parameters)),
        Result.class
    );
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

  }
}

package ink.o.w.o.resource.integration.unsplash.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ink.o.w.o.util.RequestHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

import static ink.o.w.o.resource.integration.unsplash.constant.UnsplashConstant.URL.LOCATION;

@Slf4j
@Component
public class RetrieveRandomPhotosRequest {
  public static final String REQUEST_URL = LOCATION + "/photos/random";

  @Resource
  private RequestHelper requestHelper;
  @Resource
  private RestTemplate restTemplate;

  public Object sendRequest(Parameters parameters) {
    return this.restTemplate.getForObject(
        String.format("%s%s", REQUEST_URL, requestHelper.stringifyQueryParameters(parameters)),
        Object.class
    );
  }

  public Object[] sendRequest(Parameters parameters, Integer count) {
    return this.restTemplate.getForObject(
        String.format("%s%s", REQUEST_URL, requestHelper.stringifyQueryParameters(parameters.setCount(count))),
        Object[].class
    );
  }

  @Data
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Parameters {
    @JsonProperty("client_id")
    private String clientId;

    // Public collection ID(‘s) to filter selection. If multiple, comma-separated
    private String collections;

    // Limit selection to featured photos.
    private String featured;

    // Limit selection to a single user.
    private String username;

    // Limit selection to photos matching a search term.
    private String query;

    // Filter search results by photo orientation. Valid values are landscape, portrait, and squarish.
    private String orientation;

    // The number of photos to return. (Default: 1; max: 30)
    private Integer count;
  }

  public static class Result extends HashMap<String, Object> {

  }
}

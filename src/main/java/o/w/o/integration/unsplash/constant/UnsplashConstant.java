package o.w.o.integration.unsplash.constant;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class UnsplashConstant {
  public static class URL {
    public static final String LOCATION = "https://api.unsplash.com";

    public static HttpEntity<?> getRequestHeader(String key) {
      HttpHeaders headers = new HttpHeaders();
      MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
      headers.setContentType(type);
      headers.add("Accept", MediaType.APPLICATION_JSON.toString());
      headers.add("Authorization", String.format("%s %s", "Client-ID", key));

      return new HttpEntity<>(headers);
    }
  }
}

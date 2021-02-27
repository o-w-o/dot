package o.w.o.integration.aliyun.oss.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StagedResource {
  private Integer uploaderId;
  private String urn;
  private String url;
  private Boolean status;
  private String message;

  public static StagedResource error(String message) {
    return StagedResource.builder().status(false).message(message).build();
  }
}

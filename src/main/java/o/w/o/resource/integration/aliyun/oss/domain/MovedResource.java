package o.w.o.resource.integration.aliyun.oss.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;

@Getter
@Setter
@Builder
public class MovedResource {
  private ResourceSpace resourceSpace;
  private Boolean status;
  private String message;

  public static MovedResource ok(ResourceSpace resourceSpace) {
    return MovedResource.builder()
        .status(true)
        .resourceSpace(resourceSpace)
        .build();
  }
  public static MovedResource error(String message) {
    return MovedResource.builder()
        .status(false)
        .message(message)
        .build();
  }
}

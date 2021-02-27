package o.w.o.integration.aliyun.oss.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import o.w.o.domain.core.storage.domian.FileStorage;

@Getter
@Setter
@Builder
public class MovedResource {
  private FileStorage fileStorage;
  private Boolean status;
  private String message;

  public static MovedResource ok(FileStorage resourceSpace) {
    return MovedResource.builder()
        .status(true)
        .fileStorage(resourceSpace)
        .build();
  }
  public static MovedResource error(String message) {
    return MovedResource.builder()
        .status(false)
        .message(message)
        .build();
  }
}

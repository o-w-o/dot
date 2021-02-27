package o.w.o.integration.aliyun.oss.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import o.w.o.domain.core.storage.domian.FileStorage;

@Getter
@Setter
@Builder
public class MovingResource {
  private FileStorage resourceSpace;
  private FileStorage.Stage stage;
  private String originalPath;
  private String targetPath;
}

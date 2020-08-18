package o.w.o.resource.integration.aliyun.oss.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;

@Getter
@Setter
@Builder
public class MovingResource {
  private ResourceSpace resourceSpace;
  private ResourceSpace.Stage stage;
  private String originalPath;
  private String targetPath;
}

package ink.o.w.o.resource.integration.aliyun.domain.oss;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadedOssResource {
  private Integer uploaderId;
  private Boolean status;
  private String urn;
  private String url;
}

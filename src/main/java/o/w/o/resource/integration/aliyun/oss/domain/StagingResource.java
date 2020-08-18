package o.w.o.resource.integration.aliyun.oss.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;

import java.io.File;

@Getter
@Setter
@Builder
public class StagingResource {
  @JsonIgnore
  private File file;
  private AuthorizedUser authorizedUploader;
  private String stagePath;
}

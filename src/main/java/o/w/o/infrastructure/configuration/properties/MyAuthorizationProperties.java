package o.w.o.infrastructure.configuration.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyAuthorizationProperties {
  protected String appName;
  protected String accessKeyId;
  protected String accessKeySecret;
}


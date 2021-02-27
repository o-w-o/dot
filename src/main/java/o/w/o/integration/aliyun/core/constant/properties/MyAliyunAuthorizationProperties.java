package o.w.o.integration.aliyun.core.constant.properties;

import lombok.Getter;
import lombok.Setter;
import o.w.o.infrastructure.configuration.properties.MyAuthorizationProperties;

@Setter
@Getter
public class MyAliyunAuthorizationProperties extends MyAuthorizationProperties {
  private String appName = "aliyun";
  private Boolean enable = false;
  private String regionId;
  private String endpoint;
}

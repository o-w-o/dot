package o.w.o.integration.aliyun.sts.domain;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import lombok.Data;

@Data
public class Sts {
  private AssumeRoleResponse.Credentials credentials;
  private AssumeRoleResponse.AssumedRoleUser assumedRoleUser;

  @Data
  public static class Credentials {
    private String securityToken;
    private String accessKeySecret;
    private String accessKeyId;
    private String expiration;

    public static Credentials of(AssumeRoleResponse.Credentials credentials) {
      return new Credentials()
          .setAccessKeyId(credentials.getAccessKeyId())
          .setAccessKeySecret(credentials.getAccessKeySecret())
          .setSecurityToken(credentials.getSecurityToken())
          .setExpiration(credentials.getExpiration())
          ;
    }
  }
}

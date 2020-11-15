package o.w.o.resource.system.authentication.domain;

import lombok.Data;

/**
 * AuthenticationPayload 认证载荷
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/18
 */
@Data
public class AuthenticationPayload {
  private String accessToken;
}

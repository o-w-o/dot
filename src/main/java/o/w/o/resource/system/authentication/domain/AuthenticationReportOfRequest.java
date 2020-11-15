package o.w.o.resource.system.authentication.domain;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * Request 认证报告，不包含 Token 校验
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/12
 */
@Data
public class AuthenticationReportOfRequest {
  private HttpServletRequest req;

  private String requestIp;
  private boolean reqHeaderExist = false;
  private boolean reqHeaderNonEmpty = false;
  private boolean reqHeaderPatterned = false;
  private boolean reqHeaderValid = false;

  private String jwt;
  private String message;
}

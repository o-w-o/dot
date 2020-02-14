package ink.o.w.o.server.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Getter
public enum HttpExceptionStatus {
  internalServerError(500, "发生错误，请检查API借口参数并核对其类型, 如若仍得不到解决,请联系请联系管理员！"),
  notFound(404, "资源未找到！"),
  forbidden(403, "角色权限不匹配, 可联系管理员设置相关权限！"),
  unauthorized(401, "未授权的访问！"),
  requiredRequestBodyMissing(400002, " @RequestBody 注解标识的请求体缺失！", "Required request body is missing"),
  badRequest(400, "请求异常！");

  private Integer code;
  private String message;
  private String matcher;

  HttpExceptionStatus(Integer code, String message) {
    this.code = code;
    this.message = message;
    this.matcher = "";
  }

  HttpExceptionStatus(Integer code, String message, String matcher) {
    this.code = code;
    this.message = message;
    this.matcher = matcher;
  }

  public Optional<String> getMessage(String string) {
    return Optional.ofNullable(StringUtils.contains(string, this.matcher) ? this.message : null);
  }
}

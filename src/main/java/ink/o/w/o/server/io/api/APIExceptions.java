package ink.o.w.o.server.io.api;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * APIExceptions
 *
 * @author symbols@dingtalk.com
 * @date 2020/2/14
 */
@Getter
public enum APIExceptions {
  internalServerError(500, "发生错误，请检查API借口参数并核对其类型, 如若仍得不到解决,请联系请联系管理员！"),
  notFound(404, "不存在！"),
  forbidden(403, "角色权限不匹配, 可联系管理员设置相关权限！"),
  unauthorized(401, "未授权！"),
  requiredRequestBodyMissing(400002, " @RequestBody 注解标识的请求体缺失！", "Required request body is missing"),
  badRequest(400, "请求异常！");

  private final Integer code;
  private final String message;
  private final String matcher;

  APIExceptions(Integer code, String message) {
    this.code = code;
    this.message = message;
    this.matcher = "";
  }

  APIExceptions(Integer code, String message, String matcher) {
    this.code = code;
    this.message = message;
    this.matcher = matcher;
  }

  public Optional<String> getMessage(String string) {
    return Optional.ofNullable(StringUtils.contains(string, this.matcher) ? this.message : null);
  }
}

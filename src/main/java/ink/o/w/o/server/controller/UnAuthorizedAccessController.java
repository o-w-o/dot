package ink.o.w.o.server.controller;

import ink.o.w.o.server.io.api.APIException;
import ink.o.w.o.server.io.api.APIExceptions;
import ink.o.w.o.util.HttpHelper;
import ink.o.w.o.util.JsonHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import static org.apache.commons.codec.CharEncoding.UTF_8;

/**
 * 未授权用户入口,设定控制器,统一返回 403 未授权提示, 并将提示结果封装为 RESTFUL API 的 JSON 数据.
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/2 下午11:23
 */
@Component
public class UnAuthorizedAccessController implements AuthenticationEntryPoint, Serializable {

  private static final long serialVersionUID = -8970718410437077606L;

  @Resource
  private JsonHelper jsonHelper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {

    // This is invoked when user tries to access a secured REST resource without supplying any credentials
    // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    try (PrintWriter writer = response.getWriter()) {
      writer.write(jsonHelper.toJsonString(
          APIException.unauthorized()
              .setPath(request.getRequestURI())
              .setMessage(HttpHelper.formatResponseDataMessage(request).apply(APIExceptions.forbidden.getMessage()))
      ));
      writer.flush();
    }

  }
}

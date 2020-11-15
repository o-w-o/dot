package o.w.o.server.configuration.handler;

import o.w.o.server.configuration.handler.exceptions.RequestExceptionsHandler;
import o.w.o.server.definition.ApiException;
import o.w.o.server.helper.HttpHelper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未授权用户入口，设定控制器，统一返回 401 未授权提示。
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/2 下午11:23
 * @deprecated 暂由 {@link RequestExceptionsHandler} 处理
 */
@Deprecated
@Component
public class UnAuthorizedAccessHandler implements AuthenticationEntryPoint {

  @Resource
  HttpHelper httpHelper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {
    httpHelper.handlerRequestException(request, response, ApiException.unauthorized());
  }
}

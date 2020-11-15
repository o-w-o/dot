package o.w.o.server.configuration.handler;

import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import o.w.o.server.configuration.handler.exceptions.RequestExceptionsHandler;
import o.w.o.server.definition.ApiException;
import o.w.o.server.helper.HttpHelper;
import o.w.o.server.util.ServiceUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 授权不足端口，统一返回 403 授权不足提示。
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/4 下午6:50
 * @deprecated 暂由 {@link RequestExceptionsHandler} 处理
 */
@Deprecated
@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

  @Resource
  private HttpHelper httpHelper;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException
  ) throws IOException {
    var optPrincipal = ServiceUtil.fetchAuthentication().map(Authentication::getPrincipal);
    if (optPrincipal.isPresent()) {
      var authorizedUser = (AuthorizedUser) optPrincipal.get();
      if (authorizedUser.isAnonymous()) {
        httpHelper.handlerRequestException(request, response, ApiException.unauthorized());
      } else {
        httpHelper.handlerRequestException(request, response, ApiException.forbidden());
      }
    } else {
      httpHelper.handlerRequestException(request, response, ApiException.badRequest());
    }
  }
}

package ink.o.w.o.server.controller;

import ink.o.w.o.server.domain.ResponseEntityExceptionBody;
import ink.o.w.o.util.JSONHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.commons.codec.CharEncoding.UTF_8;


/**
 * @author symbols@dingtalk.com
 * @date 2019/8/4 下午6:50
 */
@Component
public class AccessDeniedController implements AccessDeniedHandler {

  @Resource
  private JSONHelper jsonHelper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8);
    response.setStatus(HttpStatus.FORBIDDEN.value());

    try (PrintWriter writer = response.getWriter()) {
      writer.write(jsonHelper.toJSONString(
          ResponseEntityExceptionBody.forbidden()
              .setPath(request.getRequestURI())
      ));
      writer.flush();
    }
  }

}

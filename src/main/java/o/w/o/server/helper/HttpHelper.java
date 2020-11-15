package o.w.o.server.helper;

import lombok.extern.slf4j.Slf4j;
import o.w.o.server.definition.ApiException;
import o.w.o.util.HttpUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.commons.codec.CharEncoding.UTF_8;

/**
 * HttpHelper
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/19
 */
@Slf4j
@Component
public class HttpHelper {
  @Resource
  JsonHelper jsonHelper;

  /**
   * 请求异常捕获辅助类，记得必要的 return，使请求终止。
   *
   * @author symbols@dingtalk.com
   * @date 2020/11/19
   */
  public void handlerRequestException(HttpServletRequest request, HttpServletResponse response, ApiException e) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8);
    response.setStatus(e.getCode());

    try (PrintWriter writer = response.getWriter()) {
      writer.write(jsonHelper.toJsonString(
          ApiException.badRequest()
              .setMessage(HttpUtil.formatResponseExceptionMessage(request).apply(e.getMessage()))
              .setPath(request.getRequestURI())
              .setCode(e.getCode()))
      );
      writer.flush();
    }
  }

}

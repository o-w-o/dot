package ink.o.w.o.server.controller;

import ink.o.w.o.server.domain.HttpResponseDataFactory;
import ink.o.w.o.util.JSONHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author symbols@dingtalk.com
 * @date 2019/8/4 下午6:50
 */
@Component
public class AccessDeniedController implements AccessDeniedHandler {

    @Autowired JSONHelper jsonHelper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonHelper.toJSONString(
                HttpResponseDataFactory.forbidden()
                    .setPath(request.getRequestURI())
            ));
            writer.flush();
        }
    }

}

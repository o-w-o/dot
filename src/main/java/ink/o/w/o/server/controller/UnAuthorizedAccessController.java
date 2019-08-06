package ink.o.w.o.server.controller;

import ink.o.w.o.server.domain.HttpResponseData;
import ink.o.w.o.server.domain.HttpResponseDataFactory;
import ink.o.w.o.util.JSONHelper;
import ink.o.w.o.util.HttpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * 未授权用户入口,设定控制器,统一返回 403 未授权提示, 并将提示结果封装为 RESTFUL API 的JSON数据.
 *
 * @author symbols@dingtalk.com
 * @version 1.0
 * @date 2019/8/2 下午11:23
 */
@Component
public class UnAuthorizedAccessController implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Autowired
    JSONHelper jsonHelper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {

        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonHelper.toJSONString(
                HttpResponseDataFactory.unauthorized()
                    .setPath(request.getRequestURI())
                    .setMessage(HttpHelper.formatResponseDataMessage(request).apply(HttpResponseData.UNAUTHORIZED_DEFAULT_MESSAGE))
            ));
            writer.flush();
        }

    }
}

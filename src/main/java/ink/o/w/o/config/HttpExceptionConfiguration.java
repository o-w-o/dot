package ink.o.w.o.config;

import ink.o.w.o.config.domain.HttpResponseData;
import ink.o.w.o.config.exception.ServiceException;
import ink.o.w.o.util.JSONUtil;
import ink.o.w.o.util.RequestUtil;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/4 下午2:10
 */

@Slf4j
@Component
public class HttpExceptionConfiguration implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {

        HttpResponseData result = new HttpResponseData()
            .setPath(request.getRequestURI());

        Function<String, String> formatResponseDataMessage = RequestUtil.formatResponseDataMessage(request);

        if (e instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) e;
            result
                .setStatusCode(serviceException.getStatus())
                .setMessage(serviceException.getMessage());

        } else if (e instanceof AuthenticationException) {

            result.setStatusCode(HttpStatus.UNAUTHORIZED)
                .setMessage(formatResponseDataMessage.apply("禁止访问"));

        } else if (e instanceof AccessDeniedException) {

            result.setStatusCode(HttpStatus.FORBIDDEN)
                .setMessage(formatResponseDataMessage.apply("角色权限不匹配, 可联系管理员设置相关权限"));

        } else if (e instanceof NoHandlerFoundException) {

            result.setStatusCode(HttpStatus.NOT_FOUND)
                .setMessage(formatResponseDataMessage.apply("不存在"));

        } else if (e instanceof MethodArgumentTypeMismatchException) {

            result.setStatusCode(HttpStatus.BAD_REQUEST)
                .setMessage(formatResponseDataMessage.apply("参数类型不匹配"));

        } else if (e instanceof ServletException) {

            result.setStatusCode(HttpStatus.BAD_REQUEST)
                .setMessage(formatResponseDataMessage.apply(e.getMessage()));

        } else if (e instanceof MissingClaimException || e instanceof IncorrectClaimException || e instanceof SignatureException || e instanceof MalformedJwtException) {

            result.setStatusCode(HttpStatus.UNAUTHORIZED)
                .setMessage(formatResponseDataMessage.apply(e.getMessage()));

        } else {

            result.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR).setMessage(
                formatResponseDataMessage.apply("发生错误，请检查API借口参数并核对其类型, 如若仍得不到解决,请联系请联系管理员")
            );

            String message;
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                    request.getRequestURI(),
                    handlerMethod.getBean().getClass().getName(),
                    handlerMethod.getMethod().getName(),
                    e.getMessage());
            } else {
                message = e.getMessage();
            }

            logger.error(message, e);
        }

        formatsResponseResult(response, result);
        return new ModelAndView();
    }


    private void formatsResponseResult(HttpServletResponse response, HttpResponseData httpResponseData) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(httpResponseData.getStatusCode());

        try {
            response.getWriter().write(JSONUtil.toJSONString(httpResponseData));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

}

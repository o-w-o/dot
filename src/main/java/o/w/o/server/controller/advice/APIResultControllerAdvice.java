package o.w.o.server.controller.advice;

import o.w.o.server.io.api.APIResult;
import o.w.o.server.io.api.annotation.APIResource;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一 JSON 格式返回
 *
 * @author symbols@dingtalk.com
 * @date 2020/9/6
 */
@RestControllerAdvice
public class APIResultControllerAdvice implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), APIResource.class);
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
    if (body instanceof APIResult) {
      return body;
    }
    return APIResult.of(body);
  }
}

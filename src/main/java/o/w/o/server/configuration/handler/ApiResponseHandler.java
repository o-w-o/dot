package o.w.o.server.configuration.handler;

import o.w.o.server.definition.ApiResult;
import o.w.o.server.helper.ContextHelper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一 JSON 格式返回
 *
 * @author symbols@dingtalk.com
 * @date 2020/9/6
 */
@RestControllerAdvice
public class ApiResponseHandler implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
    return returnType.getContainingClass().getAnnotation(RequestMapping.class) != null
        && returnType.getContainingClass().getPackageName().contains(ContextHelper.PKG_ENTRY);
  }

  @Override
  public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
    if (body instanceof ApiResult) {
      return body;
    }
    return ApiResult.of(body);
  }
}

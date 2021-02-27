package o.w.o.domain.core.authentication.configuration;

import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.authentication.domain.RequestRateLimit;
import o.w.o.domain.core.authentication.repository.RequestRateLimitRepository;
import o.w.o.infrastructure.definition.ApiException;
import o.w.o.infrastructure.helper.HttpHelper;
import o.w.o.util.RequestUtil;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接口调用前检查 IP 抵达频次上限
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/19
 */
@Slf4j
@Component
public class RequestRateLimitPreFilter extends OncePerRequestFilter implements OrderedFilter {

  @Resource
  private HttpHelper httpHelper;

  @Resource
  private RequestRateLimitRepository requestRateLimitRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
    var optLimit = requestRateLimitRepository
        .findById(
            RequestRateLimit.generateId(RequestUtil.getIpAddress(request)).longValue()
        );

    if (optLimit.isPresent()) {
      var limit = optLimit.get();
      if (!limit.isOverTime() && limit.isOverLimit()) {
        limit.mountAfterRetryPayloadResponse(response);
        httpHelper.handlerRequestException(request, response, ApiException.of("请求次数超出限制", HttpStatus.TOO_MANY_REQUESTS));
        return;
      }
    }

    chain.doFilter(request, response);
  }

  @Override
  public int getOrder() {
    return AuthenticationTokenFilter.ORDER - 1;
  }
}

package o.w.o.resource.system.authentication.configuration;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.system.authentication.domain.RequestRateLimit;
import o.w.o.resource.system.authentication.repository.RequestRateLimitRepository;
import o.w.o.server.definition.ApiException;
import o.w.o.server.helper.HttpHelper;
import o.w.o.server.util.ServiceUtil;
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
public class RequestRateLimitPostFilter extends OncePerRequestFilter implements OrderedFilter {

  @Resource
  private HttpHelper httpHelper;

  @Resource
  private RequestRateLimitRepository requestRateLimitRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
    var uip = ServiceUtil.getPrincipalIp();
    var optUid = ServiceUtil.fetchPrincipalUserId();

    Long limitId = optUid.map(integer -> RequestRateLimit.generateId(uip, integer).longValue()).orElseGet(() -> RequestRateLimit.generateId(uip).longValue());

    var optLimit = requestRateLimitRepository.findById(limitId);
    if (optLimit.isEmpty()) {
      if (optUid.isEmpty()) {
        requestRateLimitRepository.save(RequestRateLimit.newAnonymousInstance(limitId).decrease()).mountLimitedPayloadResponse(response);
      } else {
        requestRateLimitRepository.save(RequestRateLimit.newAuthorizedInstance(limitId).decrease()).mountLimitedPayloadResponse(response);
      }
    } else {
      var limit = optLimit.get();

      if (limit.isOverTime()) {
        if (optUid.isEmpty()) {
          requestRateLimitRepository.save(RequestRateLimit.newAnonymousInstance(limitId).decrease()).mountLimitedPayloadResponse(response);
        } else {
          requestRateLimitRepository.save(RequestRateLimit.newAuthorizedInstance(limitId).decrease()).mountLimitedPayloadResponse(response);
        }
      } else if (limit.isOverLimit()) {
        limit.mountAfterRetryPayloadResponse(response);
        httpHelper.handlerRequestException(request, response, ApiException.of("请求次数超出限制", HttpStatus.TOO_MANY_REQUESTS));
        return;
      } else {
        requestRateLimitRepository.save(optLimit.get().decrease()).mountLimitedPayloadResponse(response);
      }
    }

    chain.doFilter(request, response);
  }

  @Override
  public int getOrder() {
    return AuthenticationTokenFilter.ORDER + 1;
  }
}

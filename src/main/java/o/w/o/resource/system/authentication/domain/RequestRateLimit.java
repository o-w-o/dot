package o.w.o.resource.system.authentication.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import o.w.o.resource.system.authentication.domain.property.RequestRateLimitHeader;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Data
@NoArgsConstructor
@RedisHash(value = "aj_rl", timeToLive = 60L)
public class RequestRateLimit {
  @Id
  private Long id;
  private Integer limit;
  private Integer remaining;
  private Long reset;

  public static RequestRateLimit from(HttpServletRequest request) {
    return new RequestRateLimit()
        .setLimit(Integer.parseInt(request.getHeader(RequestRateLimitHeader.LIMIT)))
        .setRemaining(Integer.parseInt(request.getHeader(RequestRateLimitHeader.REMAINING)))
        .setReset(Long.parseLong(request.getHeader(RequestRateLimitHeader.RESET)));
  }

  public static boolean isEmpty(RequestRateLimit limit) {
    return Objects.isNull(limit.limit) | Objects.isNull(limit.remaining) | Objects.isNull(limit.reset);
  }

  public static Integer generateId(String ip) {
    return ip.hashCode();
  }

  public static Integer generateId(String ip, Integer userId) {
    return (ip + userId).hashCode();
  }

  public static RequestRateLimit newAnonymousInstance(Long id) {
    return new RequestRateLimit()
        .setId(id)
        .setLimit(60)
        .setRemaining(60)
        .setReset(System.currentTimeMillis() + 60000);
  }

  public static RequestRateLimit newAuthorizedInstance(Long id) {
    return new RequestRateLimit()
        .setId(id)
        .setLimit(1000)
        .setRemaining(1000)
        .setReset(System.currentTimeMillis() + 60000);
  }

  public RequestRateLimit decrease() {
    this.setRemaining(this.getRemaining() - 1);
    return this;
  }

  public boolean isOverLimit() {
    return this.getRemaining() <= 0;
  }

  public boolean isOverTime() {
    return this.getReset() <= System.currentTimeMillis();
  }

  public RequestRateLimit mountAfterRetryPayloadResponse(HttpServletResponse response) {
    response.setHeader(RequestRateLimitHeader.RETRY_AFTER, String.valueOf(this.getReset() - System.currentTimeMillis()));

    return this;
  }

  public RequestRateLimit mountLimitedPayloadResponse(HttpServletResponse response) {
    response.setHeader(RequestRateLimitHeader.LIMIT, this.getLimit().toString());
    response.setHeader(RequestRateLimitHeader.REMAINING, this.getRemaining().toString());
    response.setHeader(RequestRateLimitHeader.RESET, this.getReset().toString());

    return this;
  }
}

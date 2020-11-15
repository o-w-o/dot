package o.w.o.resource.system.authentication.domain.property;

public class RequestRateLimitHeader {
  public static final String LIMIT = "X-Rate-Limit-Limit";
  public static final String REMAINING = "X-Rate-Limit-Remaining";
  public static final String RESET = "X-Rate-Limit-Reset";
  public static final String RETRY_AFTER = "Retry-After";
}

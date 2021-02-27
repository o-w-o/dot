package o.w.o.infrastructure.aop;

import lombok.extern.slf4j.Slf4j;
import o.w.o.infrastructure.definition.ApiException;
import o.w.o.infrastructure.definition.ApiRateLimiter;
import o.w.o.infrastructure.util.ServiceUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 接口调用频次限制器
 *
 * @author symbols@dingtalk.com
 * @date 2020/11/20
 */
@Slf4j
@Aspect
@Component
public class ApiRateLimiterAop {

  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  @Pointcut("@annotation(o.w.o.infrastructure.definition.ApiRateLimiter)")
  public void pointcut() {
  }

  @Before("pointcut()")
  public void doBefore(JoinPoint joinPoint) {
    Class<?> clazz = joinPoint.getThis().getClass();

    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();

    var uip = ServiceUtil.getPrincipalIp();
    var uid = ServiceUtil.fetchPrincipalUserId();

    // 当有 没有户 ID 时，使用用户 IP 作为替代
    String id = uid.map(String::valueOf).orElse(uip);

    var limiter = method.getAnnotation(ApiRateLimiter.class);

    var key = String.valueOf(key(clazz, method, id));

    var optRestTimes = Optional.ofNullable(get(key));

    logger.debug("接口：[{}/{}]，当前调用 ID：[{}], 记录开始：", method.getDeclaringClass(), method.getName(), id);

    if (optRestTimes.isEmpty()) {
      logger.debug("剩余次数记录为空，进行设置并记录。");
      set(key, limiter.limits(), limiter.duration(), limiter.unit());
    } else {
      var restTimes = optRestTimes.get();
      logger.debug("单位时间内调用次数：[{}/{}]", restTimes, limiter.limits());

      if (restTimes <= 0) {
        logger.debug("已无剩余次数。");
        throw ApiException.of(String.format("此接口请求次数超出设置： [%s 次  / %s min]", limiter.limits(), limiter.unit().toMinutes(limiter.duration())), HttpStatus.TOO_MANY_REQUESTS);
      } else {
        logger.debug("剩余次数减一。");
        decrement(key);
      }
    }

    logger.debug("记录结束");
  }

  public int key(Class<?> clazz, Method method, String id) {
    var str = clazz.getName() + method.hashCode() + id;
    return str.hashCode();
  }

  public void set(String key, Integer val, Long duration, TimeUnit unit) {
    redisTemplate.opsForValue().set(key, val - 1, duration, unit);
  }

  public void decrement(String key) {
    redisTemplate.opsForValue().decrement(key);
  }

  public Integer get(String key) {
    return (Integer) redisTemplate.opsForValue().get(key);
  }
}

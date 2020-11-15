package o.w.o.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
public class RequestUtil {

  /**
   * 将对象装换为 map
   *
   * @param bean -
   * @return -
   */
  public static <T> Map<String, Object> beanToMap(T bean) {
    return beanToMap(bean, true);
  }

  public static <T> Map<String, Object> beanToMap(T bean, Boolean skipNull) {
    Map<String, Object> map = Maps.newHashMap();
    BeanMap beanMap = BeanMap.create(bean);
    for (Object key : beanMap.keySet()) {
      if (skipNull) {
        if (beanMap.get(key) != null) {
          map.put(key + "", beanMap.get(key));
        }
      } else {
        map.put(key + "", beanMap.get(key));
      }
    }
    return map;
  }

  public static <T> String stringifyQueryParameters(T parameters) {
    var builder = new StringBuilder();
    try {
      var parameterMap = beanToMap(parameters);

      parameterMap
          .keySet()
          .stream()
          .map(String::valueOf)
          .filter(parameterMap::containsKey)
          .forEach((key) -> builder
              .append(builder.indexOf("?") == -1 ? "?" : "&")
              .append(key).append("=")
              .append(parameterMap.get(key))
          );
    } catch (ClassCastException | IllegalArgumentException e) {
      e.printStackTrace();
    }

    logger.info("stringifyQueryParameters -> [{}]", builder.toString());
    return builder.toString();
  }

  private static Boolean getIpAddressNextProxy(String ip) {
    return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
  }

  public static String getIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    logger.debug("getIpAddress: [USE] [x-forwarded-for] -> [{}]", ip);

    if (getIpAddressNextProxy(ip)) {
      ip = request.getRemoteAddr();
      logger.debug("getIpAddress: [USE] [RemoteAddr] -> [{}]", ip);
    }

    // 如果是多级代理，那么取第一个 ip 为客户端 ip
    if (ip != null && ip.contains(",")) {
      ip = ip.substring(0, ip.indexOf(',')).trim();
    }

    logger.info("getIpAddress: [USE] Referer: [{}], UA: [{}], IP: [{}]",
        request.getHeader("Referer"),
        request.getHeader("User-Agent"),
        ip
    );
    return ip;
  }

}

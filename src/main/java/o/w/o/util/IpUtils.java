package o.w.o.util;

import java.util.Arrays;

/**
 * IpUtils
 *
 * @author symbols@dingtalk.com
 * @date 2021/2/27
 */
public class IpUtils {
  private static final String[] LOCAL_IP_LIST = new String[]{"127.0.0.1", "0:0:0:0:0:0:0:1"};

  public static boolean isLocalIpAddress(String ipAddress) {
    return Arrays.asList(LOCAL_IP_LIST).contains(ipAddress);
  }
}

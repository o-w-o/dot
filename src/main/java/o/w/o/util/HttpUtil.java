package o.w.o.util;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午11:06
 */
public class HttpUtil {
    public static String formatResponseDataMessageByRequest(HttpServletRequest request, String supplementation) {
        return "接口 [ " + request.getRequestURI() + " ] " + supplementation;
    }

    public static Function<String, String> formatResponseExceptionMessage(HttpServletRequest request) {
        return s -> HttpUtil.formatResponseDataMessageByRequest(request, s);
    }
}

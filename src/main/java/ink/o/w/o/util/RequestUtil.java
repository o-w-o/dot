package ink.o.w.o.util;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午11:06
 */
public class RequestUtil {
    public static String formatResponseDataMessageByRequest(HttpServletRequest request, String supplementation) {
        return "接口 [" + request.getRequestURI() + "] " + supplementation;
    }

    public static Function<String, String> formatResponseDataMessage (HttpServletRequest request) {
        return new Function<String, String>() {
            @Override
            public String apply(String s) {
                return  RequestUtil.formatResponseDataMessageByRequest(request, s);
            }
        };
    }
}

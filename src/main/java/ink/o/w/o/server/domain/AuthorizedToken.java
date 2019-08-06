package ink.o.w.o.server.domain;

import lombok.Data;

/**
 * @author: symbols@dingtalk.com
 * @date: 2019/8/3 上午9:25
 */
@Data
public class AuthorizedToken {
    private String accessToken;
    private String refreshToken;
}

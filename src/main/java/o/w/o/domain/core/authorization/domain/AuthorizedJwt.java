package o.w.o.domain.core.authorization.domain;

import lombok.Data;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/3 上午9:25
 */
@Data
public class AuthorizedJwt {
    private String accessToken;
    private String refreshToken;
}

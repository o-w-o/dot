package ink.o.w.o.server.domain;

import ink.o.w.o.resource.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午7:51
 */
@Data
@NoArgsConstructor
@RedisHash("authorizedUserJwt")
public class AuthorizedJwtStore {
    @Id
    String id;

    User user;
    AuthorizedJwts authorizedJwts;

    public static String generateUuid(String aud, String jti) {
        return aud + "::" + jti;
    }
}

package ink.o.w.o.server.domain;

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
@RedisHash("authorizedJwtStore")
public class AuthorizedJwtStore {
    @Id
    String id;

    Integer userId;
    AuthorizedJwts authorizedJwts;

    public static String generateUuid(AuthorizedJwt jwt) {
        return jwt.getUid() + "::" + jwt.getJti();
    }
}

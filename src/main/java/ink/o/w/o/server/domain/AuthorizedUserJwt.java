package ink.o.w.o.server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.Map;

/**
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午7:51
 */
@Data
@NoArgsConstructor
@RedisHash
public class AuthorizedUserJwt {
    @Id
    Long id;

    Map<String, AuthorizedToken> tokenMap;
}

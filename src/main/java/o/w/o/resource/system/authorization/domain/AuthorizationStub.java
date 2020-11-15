package o.w.o.resource.system.authorization.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

/**
 * 认证数据存根
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/5 上午7:51
 */
@Data
@NoArgsConstructor
@RedisHash("aj_stub")
public class AuthorizationStub {
  @Id
  private String id;
  @Indexed
  private String ip;
  @Indexed
  private Integer uid;
  private Long jti;

  public static String generateId(Integer uid, Long jti) {
    return uid + ":" + jti;
  }

  public static String generateId(AuthorizationJwt jwt) {
    return generateId(jwt.getUid(), jwt.getJti());
  }

  public static AuthorizationStub from(AuthorizationJwt jwt) {
    return new AuthorizationStub()
        .setIp(jwt.getUip())
        .setJti(jwt.getJti())
        .setUid(jwt.getUid())
        .setId(AuthorizationStub.generateId(jwt));
  }
}

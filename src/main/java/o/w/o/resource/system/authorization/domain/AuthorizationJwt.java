package o.w.o.resource.system.authorization.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import o.w.o.resource.system.authorization.domain.property.AuthorizationPrivateClaim;
import o.w.o.resource.system.role.util.RoleUtil;
import o.w.o.resource.system.user.domain.User;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

/**
 * 授权 JWT
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/3 下午8:01
 */

@Data
public class AuthorizationJwt {
  private static final Date NBF = new Date();
  private static final KeyPair KEY = Keys.keyPairFor(SignatureAlgorithm.RS256);
  /**
   * nbf: 定义在此时间之前，JWT 不会接受处理。
   */
  private final Date nbf = NBF;
  /**
   * typ: Token 类型。
   */
  private final String typ = "jwt";
  /**
   * alg: Token 使用的加密算法。
   */
  private final String alg = "HS512";
  /**
   * iss: Token 的发行者。
   */
  private final String iss = "o-w-o.ink";
  private final Map<String, Object> claims = new HashMap<>();
  /**
   * iat: JWT 发布时间。
   */
  private Date iat;
  /**
   * exp: Token 失效期，也就是当前时间以后的某个时间本 Token 失效。
   */
  private Date exp;
  /**
   * jti: JWT 唯一标识。能用于防止 JWT 重复使用，一次只用一个 Token。
   */
  private Long jti;
  /**
   * aud: Token 的客户。
   */
  private String aud;
  /**
   * sub: Token 的主题。
   */
  private String sub;
  /**
   * 私有属性 user Id
   */
  private Integer uid;
  /**
   * 私有属性 user Ip
   */
  private String uip;
  /**
   * 私有属性 roles
   */
  private String rol;

  /**
   * Claims 是否初始化
   */
  private Boolean initialized = false;

  private AuthorizationJwt() {
    Date now = new Date();
    this
        .setJti(generateJti())
        .setAud("aud")
        .setSub("api")
        .setExp(now)
        .setIat(now)
        .setUid(0)
        .setRol("");
  }

  public static PublicKey getPublicKey() {
    return KEY.getPublic();
  }

  public static PrivateKey getPrivateKey() {
    return KEY.getPrivate();
  }

  /**
   * 生成 jwt
   *
   * @param claims claims
   * @param keepId 是否保持 jti 一致（即复制，而不是复刻）
   * @return AuthorizationJwt
   */
  private static AuthorizationJwt from(Claims claims, boolean keepId) {
    Date now = new Date();

    return new AuthorizationJwt()
        .setJti(keepId ? Long.parseLong(claims.getId()) : generateJti())
        .setAud(claims.getAudience())
        .setExp(claims.getExpiration())
        .setUid(claims.get(AuthorizationPrivateClaim.KEY_UID, Integer.class))
        .setRol(claims.get(AuthorizationPrivateClaim.KEY_ROL, String.class))
        .setUip(claims.get(AuthorizationPrivateClaim.KEY_UIP, String.class))
        .setIat(now);
  }

  public static AuthorizationJwt copy(Claims claims) {
    return from(claims, true);
  }

  public static AuthorizationJwt fork(Claims claims) {
    return from(claims, false);
  }

  private static AuthorizationJwt from(String jwt, boolean keepId) {
    return from(Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt).getBody(), keepId);
  }

  public static AuthorizationJwt fork(String jwt) {
    return from(jwt, false);
  }

  public static AuthorizationJwt copy(String jwt) {
    return from(jwt, true);
  }

  public static AuthorizationJwt from(User user) {
    return new AuthorizationJwt()
        .setAud(user.getName())
        .setRol(RoleUtil.stringfiy(user.getRoles()))
        .setUid(user.getId())
        .setIat(new Date());
  }

  public static Long generateJti() {
    var jti = new Random(UUID.randomUUID().toString().hashCode()).nextLong();
    return jti > 0 ? jti : -jti;
  }

  /**
   * 初始化 JWT —— claims
   */
  public void initialize() {
    if (!initialized) {
      claims.put(Claims.ID, this.getJti());
      claims.put(Claims.ISSUER, this.getIss());
      claims.put(Claims.AUDIENCE, this.getAud());
      claims.put(Claims.EXPIRATION, this.getExp());
      claims.put(Claims.ISSUED_AT, this.getIat());
      claims.put(Claims.NOT_BEFORE, this.getNbf());

      claims.put(AuthorizationPrivateClaim.KEY_UID, this.getUid());
      claims.put(AuthorizationPrivateClaim.KEY_ROL, this.getRol());
      claims.put(AuthorizationPrivateClaim.KEY_UIP, this.getUip());
    }
  }

  @Override
  public String toString() {
    this.initialize();

    return Jwts
        .builder()
        .setClaims(this.getClaims())
        .signWith(getPrivateKey())
        .compact();
  }
}

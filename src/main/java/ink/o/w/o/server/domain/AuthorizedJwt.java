package ink.o.w.o.server.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

import java.security.Key;
import java.security.SignatureException;
import java.util.*;

/**
 * JSON Web Token（JWT）是一种使用基于URL安全（URL-safe）的方式来传递通信双方声明（claims）的开放标准。
 * 在JWT的组成部分中，声明会被编码为一个数字签名，作为 JSON Web Signature（JWS）JSON对象或者JSON Web Encryption（JWE）明文结构的载体，
 * 用以实现Message Authentication Code（MAC，消息认证码）的完整性保护。
 * <p>
 * JWT并不等于JWS，JWS只是JWT的一种实现，除了JWS外，JWE(JSON Web Encryption)也是JWT的一种实现。
 * <p>
 * # JWT 的第一部分是 header， 头部包含了两个方面：类型和使用的哈希算法
 * <p>
 * # JWT 的第二部分是 payload，也称为 JWT Claims，这里放置的是我们需要传输的信息，有多个项目如注册的claim名称，公共claim名称和私有claim名称。
 * <p>
 * 注册claim名称有下面几个部分：
 * - iss: token的发行者
 * - sub: token的题目
 * - aud: token的客户
 * - exp: 经常使用的，以数字时间定义失效期，也就是当前时间以后的某个时间本token失效。
 * - nbf: 定义在此时间之前，JWT不会接受处理。
 * - iat: JWT发布时间，能用于决定JWT年龄
 * - jti: JWT唯一标识.能用于防止 JWT重复使用，一次只用一个token
 * <p>
 * 公共claim名称用于定义我们自己创造的信息，比如用户信息和其他重要信息。
 * 私有claim名称用于发布者和消费者都同意以私有的方式使用claim名称。
 * <p>
 * # JWT第三部分最后是签名，签名由以下组件组成：
 * <p>
 * identity payload 密钥
 * <p>
 * 下面是我们如何得到JWT的第三部分：
 * <p>
 * var encodedString = base64UrlEncode(identity) + "." + base64UrlEncode(payload); HMACSHA256(encodedString, 'secret');
 * <p>
 * 这里的secret是被服务器签名，我们服务器能够验证存在的token并签名新的token。
 * <p>
 * 摘要、签名、编码、加密是不同的概念，容易混淆了。
 * base64url的使用是把JSON编码，其实只不过是先扁平化再用64个可读无冲突字符来表达，毫无加密效果。
 * SHA256的摘要只是为JSON数据生成一个“指纹”，防止被篡改，属于完整性范畴，也无任何加密效果。
 * 摘要不等于签名，签名是用私钥加密摘要。
 * 所以Token本身并没有任何加密机制，它依赖于HTTPS的通道保密能力。
 * 不过应该可以自己为Token增加加密机制，这就带来了额外的开销。
 *
 * @author symbols@dingtalk.com
 * @date 2019/8/3 下午8:01
 */

@Data
public class AuthorizedJwt {
    private static final Key SECRET_KEY = io.jsonwebtoken.security.Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static final String REQUEST_AUTHORIZATION_KEY = "Authorization";
    public static final String AUTHORIZATION_PREFIX = "Chuancheng:";

    final String typ = "jwt";
    final String alg = "HS512";

    final String iss = "o-w-o.ink";
    String jti;
    String aud;
    Date exp;

    Integer uid;
    String rol;
    Date ctime;
    Date utime;

    public AuthorizedJwt() {
        this.setJti(UUID.randomUUID().toString());
    }

    public AuthorizedJwt(String jwt) {
        Claims claims = AuthorizedJwt.getClaimsFromJwt(jwt);
        Date now = new Date();

        this.setJti(UUID.randomUUID().toString())
            .setAud(claims.getAudience())
            .setExp(claims.getExpiration())
            .setUid(claims.get(ClaimKeys.uid, Integer.class))
            .setRol(claims.get(ClaimKeys.rol, String.class))
            .setCtime(now)
            .setUtime(now);
    }

    private Map<String, Object> getClaims() {
        Map<String, Object> claims = new HashMap<>(8);

        claims.put(ClaimKeys.jti, this.getJti());
        claims.put(ClaimKeys.iss, this.getIss());
        claims.put(ClaimKeys.aud, this.getAud());
        claims.put(ClaimKeys.exp, this.getExp());
        claims.put(ClaimKeys.uid, this.getUid());
        claims.put(ClaimKeys.rol, this.getRol());
        claims.put(ClaimKeys.ctime, this.getCtime());
        claims.put(ClaimKeys.utime, this.getUtime());

        return claims;
    }

    public String toJwt() {
        return AuthorizedJwt.generateJwt(this);
    }

    public static class ClaimKeys {
        static String jti = Claims.ID;
        static String iss = Claims.ISSUER;
        static String aud = Claims.AUDIENCE;
        static String exp = Claims.EXPIRATION;
        static String uid = "uid";
        static String rol = "rol";
        static String ctime = "ctime";
        static String utime = "utime";
    }

    public static Claims getClaimsFromJwt(String jwt) {
        return Jwts
            .parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(jwt)
            .getBody();
    }

    public static String getUid(String jwt) {
        return AuthorizedJwt.getClaimsFromJwt(jwt)
            .get(ClaimKeys.uid, String.class);
    }

    public static String getUid(@org.jetbrains.annotations.NotNull Claims claims) {
        return claims
            .get(ClaimKeys.uid, String.class);
    }

    public static Boolean hasExpired(String jwt) {
        return Calendar.getInstance().before(AuthorizedJwt.getClaimsFromJwt(jwt).get(ClaimKeys.exp, Date.class));
    }

    public static Boolean hasExpired(Claims claims) {
        return Calendar.getInstance().before(claims.get(ClaimKeys.exp, Date.class));
    }

    public static String generateJwt(AuthorizedJwt jwt) {
        return Jwts.builder()
            .setClaims(jwt.getClaims())
            .signWith(SECRET_KEY)
            .compact();
    }

    public static AuthorizedJwt generateJwt(String jwt, Boolean onlyHasNoExpired) throws SignatureException {
        if (onlyHasNoExpired && !AuthorizedJwt.hasExpired(jwt)) {
            return new AuthorizedJwt(jwt);
        }

        throw new SignatureException();
    }

    public static Boolean valid(Claims claims, AuthorizedUser user) {
        return claims.getAudience().equals(user.getUsername())
            && !AuthorizedJwt.hasExpired(claims);
    }

    public static Boolean valid(String jwt, AuthorizedUser user) {
        Claims claims = AuthorizedJwt.getClaimsFromJwt(jwt);
        return claims.getAudience().equals(user.getUsername())
            && !AuthorizedJwt.hasExpired(claims);
    }
}

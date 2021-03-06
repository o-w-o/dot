package o.w.o.integration.aliyun.sts.repository;

import com.aliyuncs.auth.sts.AssumeRoleRequest;
import lombok.extern.slf4j.Slf4j;
import o.w.o.integration.aliyun.core.AliyunOpenApiHelper;
import o.w.o.integration.aliyun.sts.domain.Sts;
import o.w.o.integration.aliyun.sts.properties.MyStsProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Component
public class AliyunStsRepository {

  @Resource
  private AliyunOpenApiHelper aliyunOpenApiHelper;

  @Resource
  private MyStsProperties myStsProperties;

  /**
   * 创建 STS
   *
   * @param roleSessionName 用户自定义参数。此参数用来区分不同的令牌，可用于用户级别的访问审计。格式：^[a-zA-Z0-9\.@\-_]+$。
   * @param policy          长度限制为 1024 字节。此参数可以限制生成的 STS token 的权限，若不指定则返回的 token 拥有指定 RAM 角色的所有权限。
   * @return -
   */
  public Optional<Sts> createSts(String roleSessionName, String policy) {
    var request = new AssumeRoleRequest();
    // roleArn 指定角色的 ARN。格式：acs:ram::$accountID:role/$roleName 。
    request.setRoleArn(myStsProperties.getRoleArn());
    request.setRoleSessionName(roleSessionName);
    request.setPolicy(policy);
    // 指定的过期时间，单位为秒。过期时间范围：900~3600秒，默认值为3600秒。
    request.setDurationSeconds(myStsProperties.getDurationSeconds());

    logger.debug("createSts: [REP] session -> [{}], policy -> [{}]", roleSessionName, policy);

    return aliyunOpenApiHelper.request(request).map(assumeRoleResponse -> new Sts()
        .setAssumedRoleUser(assumeRoleResponse.getAssumedRoleUser())
        .setCredentials(assumeRoleResponse.getCredentials()));
  }
}

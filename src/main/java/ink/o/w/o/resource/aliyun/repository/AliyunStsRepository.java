package ink.o.w.o.resource.aliyun.repository;

import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import ink.o.w.o.resource.aliyun.util.AliyunOpenAPIHelper;
import ink.o.w.o.resource.aliyun.constant.properties.MyAliyunProperties;
import ink.o.w.o.resource.aliyun.domain.Sts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class AliyunStsRepository {
  private final AliyunOpenAPIHelper aliyunOpenAPIHelper;
  private final MyAliyunProperties.MyStsProperties myStsProperties;

  @Autowired
  public AliyunStsRepository(AliyunOpenAPIHelper aliyunOpenAPIHelper, MyAliyunProperties.MyStsProperties myStsProperties) {
    this.aliyunOpenAPIHelper = aliyunOpenAPIHelper;
    this.myStsProperties = myStsProperties;
  }

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

    logger.info("createSts {} {} {}", myStsProperties.getRoleArn(), roleSessionName, policy);

    return aliyunOpenAPIHelper.request(request).map(assumeRoleResponse -> new Sts()
        .setAssumedRoleUser(assumeRoleResponse.getAssumedRoleUser())
        .setCredentials(assumeRoleResponse.getCredentials()));
  }
}

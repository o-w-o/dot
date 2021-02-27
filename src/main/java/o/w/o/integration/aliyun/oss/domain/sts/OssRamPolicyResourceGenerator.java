package o.w.o.integration.aliyun.oss.domain.sts;

import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.integration.aliyun.oss.properties.MyOssProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ResourceFactory
 *
 * @author symbols@dingtalk.com
 * @date 2020/9/9
 */

@Slf4j
@Component
public class OssRamPolicyResourceGenerator {
  @Resource
  private MyOssProperties myOssProperties;

  /**
   * 格式：acs:oss:{region}:{bucket_owner}:{bucket_name}/{object_name}
   *
   * @author symbols@dingtalk.com
   * @date 2021/2/25
   */
  public String generate(String objectName) {
    return String.format("acs:oss:*:*:%s/%s", myOssProperties.getBucketName(), objectName);
  }

  public String publicResource() {
    return generate(String.format("%s/*", myOssProperties.getPublicDir()));
  }

  public String protectResource() {
    return generate(String.format("%s/*", myOssProperties.getProtectDir()));
  }

  public String privateResource(AuthorizedUser user) {
    return privateResource(user.getId());
  }

  public String privateResource(Integer userId) {
    return generate(String.format("%s/%s/*", myOssProperties.getPrivateDir(), userId));
  }

  public String temporaryResource(AuthorizedUser user) {
    return temporaryResource(user.getId());
  }

  public String temporaryResource(Integer userId) {
    return generate(String.format("%s/%s/*", myOssProperties.getTemporaryDir(), userId));
  }

}

package o.w.o.integration.aliyun.oss.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.ResourceServiceTest;
import o.w.o.domain.core.authorization.domain.AuthorizedUser;
import o.w.o.integration.aliyun.oss.domain.sts.OssRamPolicyGenerator;
import o.w.o.integration.aliyun.oss.properties.MyOssProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class AliyunOssStsServiceTest extends ResourceServiceTest {
  @Autowired
  private AliyunOssStsService aliyunOssStsService;

  @Autowired
  private MyOssProperties myOssProperties;

  @Value("${my.ip}")
  private String myIp;

  @Test
  public void createSts() {
    var result = this.aliyunOssStsService.createStsCredentialsForUser(
        OssRamPolicyGenerator.Preset.Anonymous,
        AuthorizedUser.createAnonymousUser(this.myIp).setUsername("Test")
    ).guard();

    // 创建 OSSClient 实例。
    OSS ossClient = new OSSClientBuilder().build(this.myOssProperties.getEndpoint(), result.getAccessKeyId(), result.getAccessKeySecret(), result.getSecurityToken());

    // 设置 URL 过期时间为 1 小时。
    Date expiration = new Date(new Date().getTime() + 3600 * 1000);
    // 生成以 GET 方法访问的签名 URL，访客可以直接通过浏览器访问相关内容。
    URL url = ossClient.generatePresignedUrl(this.myOssProperties.getBucketName(), this.myOssProperties.getPublicDir() + "/images/logo.store.svg", expiration);

    // 关闭 OSSClient。
    ossClient.shutdown();

    logger.info("url -> {}", String.format("%s%s?%s", this.myOssProperties.getBucketDomain(), url.getPath(), url.getQuery()));
    assertNotNull(url);
  }
}

package o.w.o.resource.integration.aliyun.common.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.ResourceServiceTest;
import o.w.o.resource.integration.aliyun.common.constant.properties.MyAliyunProperties;
import o.w.o.resource.integration.aliyun.common.factory.PolicyFactory;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class AliyunStsServiceTest extends ResourceServiceTest {
  @Autowired
  private AliyunStsService aliyunStsService;

  @Autowired
  private MyAliyunProperties.MyOssProperties myOssProperties;

  @Value("${my.ip}")
  private String myIp;

  @Test
  public void createSts() {
    var result = this.aliyunStsService.createStsCredentialsForUser(
        PolicyFactory.Preset.Anonymous,
        AuthorizedUser.anonymousUser(this.myIp).setUsername("Test")
    ).guard();

    // 创建 OSSClient 实例。
    OSS ossClient = new OSSClientBuilder().build(this.myOssProperties.getEndpoint(), result.getAccessKeyId(), result.getAccessKeySecret(), result.getSecurityToken());

    // 设置 URL 过期时间为 1 小时。
    Date expiration = new Date(new Date().getTime() + 3600 * 1000);
    // 生成以 GET 方法访问的签名 URL，访客可以直接通过浏览器访问相关内容。
    URL url = ossClient.generatePresignedUrl(this.myOssProperties.getBucketName(), this.myOssProperties.getPublicDir() + "/logo-x.png", expiration);

    // 关闭 OSSClient。
    ossClient.shutdown();

    logger.info("url -> {}", String.format("%s%s?%s", this.myOssProperties.getBucketDomain(), url.getPath(), url.getQuery()));
    assertNotNull(url);
  }
}
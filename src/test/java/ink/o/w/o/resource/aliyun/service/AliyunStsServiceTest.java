package ink.o.w.o.resource.aliyun.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import ink.o.w.o.resource.aliyun.constant.properties.MyAliyunProperties;
import ink.o.w.o.resource.aliyun.factory.PolicyFactory;
import ink.o.w.o.resource.authorization.domain.AuthorizedUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
class AliyunStsServiceTest {
  @Autowired
  private AliyunStsService aliyunStsService;

  @Autowired
  private MyAliyunProperties.MyOssProperties myOssProperties;

  @Value("${my.ip}")
  private String myIp;

  @Test
  public void createSts() {
    var result = aliyunStsService.createStsCredentialsForUser(
        PolicyFactory.Preset.Anonymous,
        AuthorizedUser.anonymousUser(myIp).setUsername("Test")
    ).guard();

    // 创建 OSSClient 实例。
    OSS ossClient = new OSSClientBuilder().build(myOssProperties.getEndpoint(), result.getAccessKeyId(), result.getAccessKeySecret(), result.getSecurityToken());

    // 设置 URL 过期时间为 1 小时。
    Date expiration = new Date(new Date().getTime() + 3600 * 1000);
    // 生成以 GET 方法访问的签名 URL，访客可以直接通过浏览器访问相关内容。
    URL url = ossClient.generatePresignedUrl(myOssProperties.getBucketName(), myOssProperties.getPublicDir() + "/logo-x.png", expiration);

    // 关闭 OSSClient。
    ossClient.shutdown();

    logger.info("url -> {}", String.format("%s%s?%s", myOssProperties.getBucketDomain(), url.getPath(), url.getQuery()));
    assertNotNull(url);
  }
}
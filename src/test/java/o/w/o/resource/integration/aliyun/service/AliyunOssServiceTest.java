package o.w.o.resource.integration.aliyun.service;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import o.w.o.resource.integration.aliyun.constant.properties.MyAliyunProperties;
import o.w.o.resource.integration.aliyun.factory.PolicyFactory;
import o.w.o.resource.system.authorization.domain.AuthorizedUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AliyunOssServiceTest {
  @Autowired
  private AliyunStsService aliyunStsService;

  @Autowired
  private MyAliyunProperties.MyOssProperties myOssProperties;

  @Value("${my.ip}")
  private String myIp;

  @Test
  public void testOssObjectWithStyle() {
    var result = aliyunStsService.createStsCredentialsForUser(
        PolicyFactory.Preset.User_ReadAndWrite,
        AuthorizedUser.anonymousUser(myIp).setUsername("Test").setId(5)
    ).guard();

    // 创建 OSSClient 实例。
    OSS ossClient = new OSSClientBuilder().build(myOssProperties.getEndpoint(), result.getAccessKeyId(), result.getAccessKeySecret(), result.getSecurityToken());

    // 设置 URL 过期时间为 1 小时。
    Date expiration = new Date(new Date().getTime() + 3600 * 1000);
    // 设置样式
    // String style = "image/resize,m_fixed,w_100,h_100/rotate,90";/
    String style = "style/test";

    GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(myOssProperties.getBucketName(), myOssProperties.getPublicDir() + "/logo-x.png", HttpMethod.GET);
    req.setExpiration(expiration);
    req.setProcess(style);

    URL signedUrl = ossClient.generatePresignedUrl(req);

    // 关闭 OSSClient。
    ossClient.shutdown();

    logger.info("url -> {}", signedUrl);
    assertNotNull(signedUrl);
  }
}
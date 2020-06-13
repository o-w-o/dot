package o.w.o.resource.integration.aliyun.repository;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import o.w.o.resource.integration.aliyun.constant.properties.MyAliyunProperties;
import o.w.o.resource.integration.aliyun.domain.oss.TemporalOssResource;
import o.w.o.resource.integration.aliyun.domain.oss.UploadedOssResource;
import o.w.o.resource.integration.aliyun.service.AliyunStsService;
import o.w.o.server.io.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class AliyunOssRepository {
  @Resource
  MyAliyunProperties.MyOssProperties myOssProperties;

  @Resource
  AliyunStsService aliyunStsService;

  public UploadedOssResource upload(TemporalOssResource o) {
    var result = this.aliyunStsService.createStsCredentialsByPolicy(
        TemporalOssResource.OssStsPolicy.getInstance(o.getAuthorizedUploader()).getPolicy(),
        o.getAuthorizedUploader()
    );

    // 创建 OSSClient 实例
    OSS ossClient = new OSSClientBuilder()
        .build(this.myOssProperties.getEndpoint(), result.getPayload().getAccessKeyId(), result.getPayload().getAccessKeySecret(), result.getPayload().getSecurityToken());

    try {
      try (InputStream inputStream = new FileInputStream(o.getFile())) {
        var url = String.format("%s/%s/%s", this.myOssProperties.getTemporaryDir(), o.getAuthorizedUploader().getId(), o.getFile().getName());
        PutObjectResult putObjectResult = ossClient
            .putObject(
                this.myOssProperties.getBucketName(),
                url,
                inputStream
            );

        // 关闭 OSSClient
        ossClient.shutdown();

        return UploadedOssResource.builder()
            .urn(putObjectResult.getETag())
            .status(true)
            .uploaderId(o.getAuthorizedUploader().getId())
            .url(String.format("%s/%s", this.myOssProperties.getBucketDomain(), url))
            .build();
      } catch (FileNotFoundException e) {
        logger.error("文件丢失！", e);
        throw ServiceException.of("文件丢失！");
      }
    } catch (IOException e) {
      logger.error("文件读取异常！", e);
      throw ServiceException.of("文件读取异常！");
    }

  }
}

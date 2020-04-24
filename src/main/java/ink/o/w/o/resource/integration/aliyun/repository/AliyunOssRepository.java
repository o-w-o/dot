package ink.o.w.o.resource.integration.aliyun.repository;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import ink.o.w.o.resource.integration.aliyun.constant.properties.MyAliyunProperties;
import ink.o.w.o.resource.integration.aliyun.domain.oss.TemporalOssResource;
import ink.o.w.o.resource.integration.aliyun.domain.oss.UploadedOssResource;
import ink.o.w.o.resource.integration.aliyun.service.AliyunStsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
@Component
public class AliyunOssRepository {
  @Autowired
  MyAliyunProperties.MyOssProperties myOssProperties;

  @Autowired
  AliyunStsService aliyunStsService;

  public UploadedOssResource upload(TemporalOssResource o) {
    var result = aliyunStsService.createStsCredentialsByPolicy(
        TemporalOssResource.OssStsPolicy.getInstance(o.getAuthorizedUploader()).getPolicy(),
        o.getAuthorizedUploader()
    );

    // 创建 OSSClient 实例
    OSS ossClient = new OSSClientBuilder()
        .build(myOssProperties.getEndpoint(), result.getPayload().getAccessKeyId(), result.getPayload().getAccessKeySecret(), result.getPayload().getSecurityToken());

    // 上传文件流
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(o.getFile());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    var url = String.format("%s/%s/%s", myOssProperties.getTemporaryDir(), o.getAuthorizedUploader().getId(), o.getFile().getName());
    PutObjectResult putObjectResult = ossClient
        .putObject(
            myOssProperties.getBucketName(),
            url,
            inputStream
        );

    // 关闭 OSSClient
    ossClient.shutdown();

    return UploadedOssResource.builder()
        .urn(putObjectResult.getETag())
        .status(true)
        .uploaderId(o.getAuthorizedUploader().getId())
        .url(String.format("%s/%s", myOssProperties.getBucketDomain(), url))
        .build();
  }
}

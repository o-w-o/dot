package o.w.o.resource.integration.aliyun.oss.repository;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.integration.aliyun.common.constant.properties.MyAliyunProperties;
import o.w.o.resource.integration.aliyun.common.service.AliyunStsService;
import o.w.o.resource.integration.aliyun.oss.domain.MovedResource;
import o.w.o.resource.integration.aliyun.oss.domain.MovingResource;
import o.w.o.resource.integration.aliyun.oss.domain.StagedResource;
import o.w.o.resource.integration.aliyun.oss.domain.StagingResource;
import o.w.o.resource.integration.aliyun.oss.util.OssStageStsPolicyGenerator;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.server.io.service.ServiceContext;
import o.w.o.server.io.service.ServiceException;
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

  public StagedResource stage(StagingResource o) {
    var ossStsPolicy = OssStageStsPolicyGenerator.OssStsPolicy.getInstance(o.getAuthorizedUploader(), ResourceSpace.Stage.STAGING);
    var result = this.aliyunStsService.createStsCredentialsByPolicy(
        ossStsPolicy.getPolicy(),
        ossStsPolicy.getUser()
    );

    // 创建 OSSClient 实例
    OSS ossClient = new OSSClientBuilder()
        .build(this.myOssProperties.getEndpoint(), result.getPayload().getAccessKeyId(), result.getPayload().getAccessKeySecret(), result.getPayload().getSecurityToken());

    try {
      try (InputStream inputStream = new FileInputStream(o.getFile())) {
        try {
          var req = new PutObjectRequest(this.myOssProperties.getBucketName(), o.getStagePath(), inputStream);

          ObjectMetadata meta = new ObjectMetadata();

          // 设置内容被下载时的名称。
          meta.setContentDisposition(o.getFile().getName());

          req.setMetadata(meta);

          PutObjectResult putObjectResult = ossClient
              .putObject(req);

          return StagedResource.builder()
              .urn(putObjectResult.getETag())
              .status(true)
              .uploaderId(o.getAuthorizedUploader().getId())
              .url(String.format("%s/%s", this.myOssProperties.getBucketDomain(), o.getStagePath()))
              .build();
        } catch (OSSException e) {
          return StagedResource.error(e.getErrorMessage());
        } catch (ClientException e) {
          return StagedResource.error(e.getErrorMessage());
        } finally {
          // 关闭 OSSClient
          ossClient.shutdown();
        }
      } catch (FileNotFoundException e) {
        logger.error("文件丢失！", e);
        throw ServiceException.of("文件丢失！");
      }
    } catch (IOException e) {
      logger.error("文件读取异常！", e);
      throw ServiceException.of("文件读取异常！");
    }
  }

  public MovedResource move(MovingResource o) {
    var user = ServiceContext.getAuthorizedUserFormSecurityContext();

    var ossStsPolicy = OssStageStsPolicyGenerator.OssStsPolicy.getInstance(user, o.getStage());
    var result = this.aliyunStsService.createStsCredentialsByPolicy(
        ossStsPolicy.getPolicy(),
        ossStsPolicy.getUser()
    );

    // 创建 OSSClient 实例
    OSS ossClient = new OSSClientBuilder()
        .build(this.myOssProperties.getEndpoint(), result.getPayload().getAccessKeyId(), result.getPayload().getAccessKeySecret(), result.getPayload().getSecurityToken());

    // 设置源文件与目标文件相同，调用 ossClient.copyObject 方法修改文件元信息。
    CopyObjectRequest request = new CopyObjectRequest(myOssProperties.getBucketName(), o.getOriginalPath(), myOssProperties.getBucketName(), o.getTargetPath());

    try {
      // 修改元信息。
      ossClient.copyObject(request);

      return MovedResource.ok(
          o.getResourceSpace()
              .setStage(ResourceSpace.Stage.PERSISTED)
              .setUrl(String.format("%s/%s", this.myOssProperties.getBucketDomain(), o.getTargetPath()))
      );
    } catch (OSSException e) {
      return MovedResource.error(e.getErrorMessage());
    } catch (ClientException e) {
      return MovedResource.error(e.getErrorMessage());
    } finally {
      // 关闭 OSSClient
      ossClient.shutdown();
    }
  }
}

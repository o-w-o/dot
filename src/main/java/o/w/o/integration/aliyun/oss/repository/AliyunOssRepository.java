package o.w.o.integration.aliyun.oss.repository;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import o.w.o.domain.core.storage.domian.FileStorage;
import o.w.o.infrastructure.definition.ServiceException;
import o.w.o.infrastructure.util.ServiceUtil;
import o.w.o.integration.aliyun.oss.domain.MovedResource;
import o.w.o.integration.aliyun.oss.domain.MovingResource;
import o.w.o.integration.aliyun.oss.domain.StagedResource;
import o.w.o.integration.aliyun.oss.domain.StagingResource;
import o.w.o.integration.aliyun.oss.properties.MyOssProperties;
import o.w.o.integration.aliyun.oss.service.AliyunOssStsService;
import o.w.o.integration.aliyun.oss.util.OssStsPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;

@Slf4j
@Component
public class AliyunOssRepository {
  @Resource
  MyOssProperties myOssProperties;
  @Resource
  OssStsPolicy ossStsPolicy;
  @Resource
  AliyunOssStsService aliyunOssStsService;
  @Value("${my.store.dir}")
  private String ossLocalStorageDir;

  public StagedResource stage(StagingResource o) {
    var ossStsPolicy = this.ossStsPolicy.generatePolicy(o.getAuthorizedUploader(), FileStorage.Stage.STAGING);
    var result = this.aliyunOssStsService.createStsCredentialsByPolicy(
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
    var user = ServiceUtil.getPrincipal();

    var ossStsPolicy = this.ossStsPolicy.generatePolicy(user, o.getStage());
    var result = this.aliyunOssStsService.createStsCredentialsByPolicy(
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
              .setStage(FileStorage.Stage.PERSISTED)
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

  public File download(String ossRemotePath) {
    var ossLocalStoragePath = ossLocalStorageDir + "/" + ossRemotePath;

    if(fetch(ossRemotePath)) {
      return new File(ossLocalStoragePath);
    } else {
      throw ServiceException.of("文件获取失败！");
    }
  }

  public boolean fetch(String ossRemotePath) {
    var ossLocalStoragePath = ossLocalStorageDir + "/" + ossRemotePath;
    var ossStsPolicy = this.ossStsPolicy.generatePolicy(ServiceUtil.getPrincipal(), FileStorage.Stage.PERSISTED);

    var ossCredentials = this.aliyunOssStsService.createStsCredentialsByPolicy(
        ossStsPolicy.getPolicy(),
        ossStsPolicy.getUser()
    );

    // 创建 OSSClient 实例
    OSS ossClient = new OSSClientBuilder()
        .build(this.myOssProperties.getEndpoint(), ossCredentials.getPayload().getAccessKeyId(), ossCredentials.getPayload().getAccessKeySecret(), ossCredentials.getPayload().getSecurityToken());

    var ossObject = ossClient.getObject(new GetObjectRequest(this.myOssProperties.getBucketName(), ossRemotePath));

    try {
      var file = new File(ossLocalStoragePath);
      file.getParentFile().mkdirs();
      file.createNewFile();

      ossObject.getObjectContent().transferTo(new FileOutputStream(ossLocalStoragePath));
    } catch (IOException e) {
      e.printStackTrace();
    }

    ossClient.shutdown();

    return FileUtil.exist(ossLocalStoragePath);
  }
}

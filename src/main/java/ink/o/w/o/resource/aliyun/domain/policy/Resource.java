package ink.o.w.o.resource.aliyun.domain.policy;

import com.fasterxml.jackson.annotation.JsonValue;
import ink.o.w.o.resource.aliyun.constant.properties.MyAliyunProperties;
import ink.o.w.o.resource.user.domain.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component("PolicyResource")
public class Resource {

  private static MyAliyunProperties.MyOssProperties myOssProperties;
  private String bucketName;
  private String dirName;

  public static Resource publicResource() {
    return new Resource()
        .setBucketName(myOssProperties.getBucketName())
        .setDirName(myOssProperties.getPublicDir());
  }

  public static Resource protectResource() {
    return new Resource()
        .setBucketName(myOssProperties.getBucketName())
        .setDirName(myOssProperties.getProtectDir());
  }

  public static Resource privateResource(User user) {
    return new Resource()
        .setBucketName(myOssProperties.getBucketName())
        .setDirName(String.format("%s/%s", myOssProperties.getPrivateDir(), user.getId()));
  }

  public static Resource temporaryResource(User user) {
    return new Resource()
        .setBucketName(myOssProperties.getBucketName())
        .setDirName(String.format("%s/%s", myOssProperties.getTemporaryDir(), user.getId()));
  }

  @Autowired
  public void setHost(MyAliyunProperties.MyOssProperties myOssProperties) {
    Resource.myOssProperties = myOssProperties;
  }

  @JsonValue
  public String toJson() {
    return dirName == null
        ? String.format("acs:oss:*:*:%s", bucketName)
        : String.format("acs:oss:*:*:%s/%s/*", bucketName, dirName);
  }
}

package o.w.o.resource.integration.aliyun.common.domain.policy;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import o.w.o.resource.integration.aliyun.common.constant.properties.MyAliyunProperties;
import o.w.o.resource.system.user.domain.User;
import o.w.o.server.io.system.SystemContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Resource
 *
 * @author symbols@dingtalk.com
 * @date 2020/7/1
 */

@NoArgsConstructor
@Data
@Component
public class Resource {

  protected String bucketName;
  protected String dirName;

  private MyAliyunProperties.MyOssProperties myOssProperties;

  @Autowired
  public Resource setMyOssProperties(MyAliyunProperties.MyOssProperties myOssProperties) {
    this.myOssProperties = myOssProperties;
    return this;
  }

  @NoArgsConstructor
  @Setter
  @Getter
  public static class MyOssResource extends Resource {
    public static final Resource resource = SystemContext.getBean(Resource.class);

    public static MyOssResource publicResource() {
      var res = new MyOssResource();
      res.setBucketName(resource.getMyOssProperties().getBucketName())
          .setDirName(String.format("%s/*", resource.getMyOssProperties().getPublicDir()));

      return res;
    }

    public static MyOssResource protectResource() {
      var res = new MyOssResource();
      res.setBucketName(resource.getMyOssProperties().getBucketName())
          .setDirName(String.format("%s/*", resource.getMyOssProperties().getProtectDir()));

      return res;
    }

    public static MyOssResource privateResource(User user) {
      return privateResource(user.getId());
    }

    public static MyOssResource privateResource(Integer userId) {
      var res = new MyOssResource();
      res.setBucketName(resource.getMyOssProperties().getBucketName())
          .setDirName(String.format("%s/%s/*", resource.getMyOssProperties().getPrivateDir(), userId));

      return res;
    }

    public static MyOssResource temporaryResource(User user) {
      return temporaryResource(user.getId());
    }

    public static MyOssResource temporaryResource(Integer userId) {
      var res = new MyOssResource();
      res.setBucketName(resource.getMyOssProperties().getBucketName())
          .setDirName(String.format("%s/%s/*", resource.getMyOssProperties().getTemporaryDir(), userId));

      return res;
    }

    @JsonValue
    public String toJson() {
      return this.dirName == null
          ? String.format("acs:oss:*:*:%s", this.bucketName)
          : String.format("acs:oss:*:*:%s/%s", this.bucketName, this.dirName);
    }

  }
}

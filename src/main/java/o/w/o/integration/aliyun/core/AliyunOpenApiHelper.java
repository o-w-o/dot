package o.w.o.integration.aliyun.core;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import o.w.o.infrastructure.definition.ServiceException;
import o.w.o.integration.aliyun.core.constant.properties.MyAliyunAuthorizationProperties;
import o.w.o.integration.aliyun.sts.properties.MyStsProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public class AliyunOpenApiHelper {

  @Resource
  private MyStsProperties myStsProperties;


  public <T extends AcsResponse> Optional<T> request(AcsRequest<T> requestPayload) {
    return new Request<T>(myStsProperties).setRequestPayload(requestPayload).sendRequest();
  }

  public <T extends AcsResponse> Optional<T> request(AcsRequest<T> requestPayload, MyAliyunAuthorizationProperties properties) {
    return new Request<T>(properties).setRequestPayload(requestPayload).sendRequest();
  }

  public <T extends AcsResponse> Function<AcsRequest<T>, Optional<T>> request(MyAliyunAuthorizationProperties properties) {
    return s -> request(s, properties);
  }

  @Data
  public static class Request<T extends AcsResponse> {
    private MyAliyunAuthorizationProperties myAliyunAuthorizationProperties;

    private DefaultProfile profile;
    private IAcsClient client;

    private AcsRequest<T> requestPayload;

    public Request(MyAliyunAuthorizationProperties myAliyunAuthorizationProperties) {
      this.myAliyunAuthorizationProperties = myAliyunAuthorizationProperties;
      this.profile = DefaultProfile.getProfile(myAliyunAuthorizationProperties.getRegionId(), myAliyunAuthorizationProperties.getAccessKeyId(), myAliyunAuthorizationProperties.getAccessKeySecret());
      this.client = new DefaultAcsClient(this.profile);
    }

    private Request<T> setRequestPayload(AcsRequest<T> requestPayload) {
      this.requestPayload = requestPayload;
      this.requestPayload.setRegionId(myAliyunAuthorizationProperties.getRegionId());
      return this;
    }

    public Optional<T> sendRequest() {
      try {
        return Optional.ofNullable(this.client.getAcsResponse(requestPayload));
      } catch (ClientException e) {
        logger.error(
            "ClientException -> [ {} ], \nrequestPayload -> {}\n{}",
            e.getErrMsg(),
            requestPayload.getClass().getName(),
            new GsonBuilder().setPrettyPrinting().create().toJson(requestPayload)
        );

        throw ServiceException.of("获取授权请求失败！" + e.getErrMsg());
      }
    }
  }
}

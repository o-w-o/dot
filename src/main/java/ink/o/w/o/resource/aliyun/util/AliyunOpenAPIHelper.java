package ink.o.w.o.resource.aliyun.util;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.GsonBuilder;
import ink.o.w.o.resource.aliyun.constant.properties.MyAliyunProperties;
import ink.o.w.o.server.exception.ServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public class AliyunOpenAPIHelper {
  private final MyAliyunProperties.MyStsProperties myStsProperties;

  @Autowired
  AliyunOpenAPIHelper(MyAliyunProperties.MyStsProperties myStsProperties) {
    this.myStsProperties = myStsProperties;
  }

  public <T extends AcsResponse> Optional<T> request(AcsRequest<T> requestPayload) {
    return new Request<T>(myStsProperties).setRequestPayload(requestPayload).sendRequest();
  }

  public <T extends AcsResponse> Optional<T> request(AcsRequest<T> requestPayload, MyAliyunProperties.MyAuthorizationProperties properties) {
    return new Request<T>(properties).setRequestPayload(requestPayload).sendRequest();
  }

  public <T extends AcsResponse> Function<AcsRequest<T>, Optional<T>> request(MyAliyunProperties.MyAuthorizationProperties properties) {
    return s -> request(s, properties);
  }

  @Data
  public static class Request<T extends AcsResponse> {
    private MyAliyunProperties.MyAuthorizationProperties myAuthorizationProperties;

    private DefaultProfile profile;
    private IAcsClient client;

    private AcsRequest<T> requestPayload;

    public Request(MyAliyunProperties.MyAuthorizationProperties myAuthorizationProperties) {
      this.myAuthorizationProperties = myAuthorizationProperties;
      this.profile = DefaultProfile.getProfile(myAuthorizationProperties.getRegionId(), myAuthorizationProperties.getAccessKeyId(), myAuthorizationProperties.getAccessKeySecret());
      this.client = new DefaultAcsClient(this.profile);
    }

    private Request<T> setRequestPayload(AcsRequest<T> requestPayload) {
      this.requestPayload = requestPayload;
      this.requestPayload.setSysRegionId(myAuthorizationProperties.getRegionId());
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

        throw new ServiceException("获取授权请求失败！" + e.getErrMsg());
      }
    }
  }
}

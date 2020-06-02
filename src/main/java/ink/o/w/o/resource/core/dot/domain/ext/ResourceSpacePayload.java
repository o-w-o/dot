package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpacePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonTypedSpacePayload
public abstract class ResourceSpacePayload {
  private ResourceSpacePayloadType payloadType;

  /**
   * 音频单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @EqualsAndHashCode(callSuper = true)
  @NoArgsConstructor
  @Data

  @JsonTypeName(ResourceSpacePayloadType.TypeName.RESOURCE_AUDIO)
  public static class Audio extends ResourceSpacePayload {
    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.RESOURCE_AUDIO);
    }
  }

  /**
   * 二进制文件单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(ResourceSpacePayloadType.TypeName.RESOURCE_BINARY)
  public static class Binary extends ResourceSpacePayload {
    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.RESOURCE_BINARY);
    }
  }

  /**
   * 图片单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(ResourceSpacePayloadType.TypeName.RESOURCE_PICTURE)
  public static class Picture extends ResourceSpacePayload {
    private String uuid;

    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.RESOURCE_PICTURE);
    }
  }

  /**
   * 文本文件单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(ResourceSpacePayloadType.TypeName.RESOURCE_TEXT)
  public static class Text extends ResourceSpacePayload {
    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.RESOURCE_TEXT);
    }
  }

  /**
   * 视频单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(ResourceSpacePayloadType.TypeName.RESOURCE_VIDEO)
  public static class Video extends ResourceSpacePayload {
    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.RESOURCE_VIDEO);
    }
  }
}

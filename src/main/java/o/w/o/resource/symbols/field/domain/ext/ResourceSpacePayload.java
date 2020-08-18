package o.w.o.resource.symbols.field.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import o.w.o.server.io.json.annotation.JsonTypeTargetPayloadType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@JsonTypeTargetPayloadType
public abstract class ResourceSpacePayload {
  private ResourceSpacePayloadType payloadType;

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

  @JsonTypeName(ResourceSpacePayloadType.TypeName.TEXT)
  public static class Text extends ResourceSpacePayload {
    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.TEXT);
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

  @JsonTypeName(ResourceSpacePayloadType.TypeName.BINARY)
  public static class Binary extends ResourceSpacePayload {
    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.BINARY);
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

  @JsonTypeName(ResourceSpacePayloadType.TypeName.PICTURE)
  public static class Picture extends ResourceSpacePayload {
    private String thumbnailUrl;

    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.PICTURE);
    }
  }

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

  @JsonTypeName(ResourceSpacePayloadType.TypeName.AUDIO)
  public static class Audio extends ResourceSpacePayload {
    @NotNull
    @Min(0)
    private Integer duration;
    private String subtitles;
    private String coverPictureUrl;

    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.AUDIO);
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

  @JsonTypeName(ResourceSpacePayloadType.TypeName.VIDEO)
  public static class Video extends ResourceSpacePayload {
    @NotNull
    @Min(0)
    private Integer duration;
    private String subtitles;
    private String coverPictureUrl;

    @Override
    public ResourceSpacePayloadType getPayloadType() {
      return new ResourceSpacePayloadType(ResourceSpacePayloadType.TypeEnum.VIDEO);
    }
  }
}

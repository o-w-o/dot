package o.w.o.resource.symbols.field.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.server.io.json.annotation.JsonTypeTargetPayloadType;

import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

/**
 * 文本空间
 *
 * @author symbols@dingtalk.com
 * @date 2020/08/09
 * @since 1.0.0
 */
@Data
@JsonTypeTargetPayloadType
public abstract class TextSpacePayload {
  private TextSpacePayloadType payloadType;

  /**
   * 标题单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @EqualsAndHashCode(callSuper = true)
  @NoArgsConstructor
  @Data

  @JsonTypeName(TextSpacePayloadType.TypeName.HEADING)
  public static class Heading extends TextSpacePayload {
    private String content;

    @Enumerated
    private Level level;

    @Override
    public TextSpacePayloadType getPayloadType() {
      return new TextSpacePayloadType(TextSpacePayloadType.TypeEnum.HEADING);
    }

    public enum Level {
      NONE(0),
      L1(1),
      L2(2),
      L3(3),
      L4(4),
      L5(5),
      ;

      @Getter
      private final Integer order;

      Level(Integer order) {
        this.order = order;
      }
    }
  }

  /**
   * 段落单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(TextSpacePayloadType.TypeName.PARAGRAPH)
  public static class Paragraph extends TextSpacePayload {
    @Override
    public TextSpacePayloadType getPayloadType() {
      return new TextSpacePayloadType(TextSpacePayloadType.TypeEnum.PARAGRAPH);
    }
  }

  /**
   * 摘要单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(TextSpacePayloadType.TypeName.SUMMARY)
  public static class Summary extends TextSpacePayload {
    @Override
    public TextSpacePayloadType getPayloadType() {
      return new TextSpacePayloadType(TextSpacePayloadType.TypeEnum.SUMMARY);
    }
  }

  /**
   * 引用单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(TextSpacePayloadType.TypeName.REFERENCE)
  public static class Reference extends TextSpacePayload {
    @ManyToMany
    private Set<Field> referenceDots;

    @Override
    public TextSpacePayloadType getPayloadType() {
      return new TextSpacePayloadType(TextSpacePayloadType.TypeEnum.REFERENCE);
    }
  }

  /**
   * 嵌入单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(TextSpacePayloadType.TypeName.EMBED)
  public static class Embed extends TextSpacePayload {
    private String embedLink;
    private String originLink;
    private EmbedType originType;

    @Override
    public TextSpacePayloadType getPayloadType() {
      return new TextSpacePayloadType(TextSpacePayloadType.TypeEnum.EMBED);
    }

    public enum EmbedType {
      VIDEO_BILIBILI(EmbedTypeName.BILIBILI),
      AUDIO_XIAMI(EmbedTypeName.XIAMI),
      TOOL_FIGMA(EmbedTypeName.FIGMA),
      TOOL_AMAP(EmbedTypeName.AMAP),
      TOOL_CODE_PEN(EmbedTypeName.CODE_PEN),
      ;

      @Getter
      private final String typeName;

      EmbedType(String typeName) {
        this.typeName = typeName;
      }
    }

    public static class EmbedTypeName {
      private static final String FIGMA = "FIGMA";
      private static final String BILIBILI = "BILIBILI";
      private static final String XIAMI = "XIAMI";
      private static final String AMAP = "AMAP";
      private static final String CODE_PEN = "CODE_PEN";
    }
  }

  /**
   * 嵌入单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(TextSpacePayloadType.TypeName.RESOURCE)
  public static class Resource extends TextSpacePayload {
    @ManyToOne
    private Field dependentDot;
    private ResourceType resourceType;

    @Override
    public TextSpacePayloadType getPayloadType() {
      return new TextSpacePayloadType(TextSpacePayloadType.TypeEnum.RESOURCE);
    }

    public enum ResourceType {
      INNER(ResourceTypeName.INNER),
      OUTER(ResourceTypeName.OUTER),
      ;

      @Getter
      private final String typeName;

      ResourceType(String typeName) {
        this.typeName = typeName;
      }
    }

    public static class ResourceTypeName {
      private static final String INNER = "INNER";
      private static final String OUTER = "OUTER";
    }
  }

  /**
   * 链接单元
   *
   * @author symbols@dingtalk.com
   * @date 2020/02/12 16:48
   * @since 1.0.0
   */
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Data

  @JsonTypeName(TextSpacePayloadType.TypeName.LINK)
  public static class Link extends TextSpacePayload {
    private String link;
    private String href;

    @Override
    public TextSpacePayloadType getPayloadType() {
      return new TextSpacePayloadType(TextSpacePayloadType.TypeEnum.LINK);
    }
  }
}

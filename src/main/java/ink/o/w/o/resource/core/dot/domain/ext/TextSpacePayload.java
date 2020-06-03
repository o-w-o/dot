package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpacePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;

@Data
@JsonTypedSpacePayload
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
      FIGMA(EmbedTypeName.XIAMI),
      BILIBILI(EmbedTypeName.BILIBILI),
      XIAMI(EmbedTypeName.FIGMA),
      GAODE_DITU(EmbedTypeName.GAODE_DITU),
      CODE_PEN(EmbedTypeName.CODE_PEN),
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
      private static final String GAODE_DITU = "GAODE_DITU";
      private static final String CODE_PEN = "CODE_PEN";
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
  public static class LINK extends TextSpacePayload {
    private String link;
    private CopyrightType copyrightType;
    private OriginType originType;

    @Override
    public TextSpacePayloadType getPayloadType() {
      return new TextSpacePayloadType(TextSpacePayloadType.TypeEnum.LINK);
    }

    public enum CopyrightType {
      NONE(CopyrightTypeName.FREE),
      UNKNOWN(CopyrightTypeName.UNKNOWN),
      FREE(CopyrightTypeName.NONE),
      VRF(CopyrightTypeName.VRF),
      COMMERCIAL(CopyrightTypeName.COMMERCIAL),
      ;

      @Getter
      private String typeName;

      CopyrightType(String typeName) {
        this.typeName = typeName;
      }
    }

    public enum OriginType {
      HEADING(OriginTypeName.HEADING),
      SUMMARY(OriginTypeName.SUMMARY),
      PARAGRAPH(OriginTypeName.PARAGRAPH),
      MATH_ML(OriginTypeName.MATH_ML),
      CODE(OriginTypeName.CODE),
      ;

      @Getter
      private String typeName;

      OriginType(String typeName) {
        this.typeName = typeName;
      }
    }

    public static class CopyrightTypeName {
      private static final String NONE = "NONE";
      private static final String UNKNOWN = "UNKNOWN";
      private static final String FREE = "FREE";
      private static final String VRF = "VRF";
      private static final String COMMERCIAL = "COMMERCIAL";
    }

    public static class OriginTypeName {
      private static final String PARAGRAPH = "PARAGRAPH";
      private static final String SUMMARY = "SUMMARY";
      private static final String HEADING = "HEADING";
      private static final String MATH_ML = "MATH_ML";
      private static final String CODE = "CODE";
    }
  }
}

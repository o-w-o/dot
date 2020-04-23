package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 链接单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:01
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.LINK)

@Entity
@Table(name = "t_dot__link_picture")
public class LinkPictureDot extends DotSpace {
  @Override
  public DotType.DotTypeEnum getType() {
    return DotType.DotTypeEnum.LINK;
  }

  private String link;
  private CopyrightType copyrightType;
  private OriginType originType;

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

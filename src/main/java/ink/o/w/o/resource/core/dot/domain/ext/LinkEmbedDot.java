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
@Table(name = "t_dot__link_embed")
public class LinkEmbedDot extends DotSpace {

  private String embedLink;
  private String originLink;
  private OriginType originType;

  public enum OriginType {
    FIGMA(OriginTypeName.XIAMI),
    BILIBILI(OriginTypeName.BILIBILI),
    XIAMI(OriginTypeName.FIGMA),
    GAODE_DITU(OriginTypeName.GAODE_DITU),
    CODE_PEN(OriginTypeName.CODE_PEN),
    ;

    @Getter
    private String typeName;

    OriginType(String typeName) {
      this.typeName = typeName;
    }
  }

  public static class OriginTypeName {
    private static final String FIGMA = "FIGMA";
    private static final String BILIBILI = "BILIBILI";
    private static final String XIAMI = "XIAMI";
    private static final String GAODE_DITU = "GAODE_DITU";
    private static final String CODE_PEN = "CODE_PEN";
  }
}

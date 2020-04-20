package ink.o.w.o.resource.core.dot.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ink.o.w.o.resource.core.dot.domain.DotType;
import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.DotSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * 文本单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:03
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(DotType.TypeName.TEXT)

@Entity
@Table(name = "t_dot__text")
public class TextDot extends DotSpace {
  @Enumerated
  private TextType textType;

  @Enumerated
  private TextLevel textLevel;

  @Lob
  private String textContent;

  @ManyToMany
  private Set<Dot> dependentDots;

  @ManyToMany
  private Set<Dot> referenceDots;

  public enum TextLevel {
    NONE(0),
    L1(1),
    L2(2),
    L3(3),
    L4(4),
    L5(5),
    ;

    @Getter
    private Integer order;

    TextLevel(Integer order) {
      this.order = order;
    }
  }

  public enum TextType {
    HEADING(TextTypeName.HEADING),
    SUMMARY(TextTypeName.SUMMARY),
    PARAGRAPH(TextTypeName.PARAGRAPH),
    MATH_ML(TextTypeName.MATH_ML),
    CODE(TextTypeName.CODE),
    ;

    @Getter
    private String typeName;

    TextType(String typeName) {
      this.typeName = typeName;
    }
  }

  public static class TextTypeName {
    private static final String PARAGRAPH = "PARAGRAPH";
    private static final String SUMMARY = "SUMMARY";
    private static final String HEADING = "HEADING";
    private static final String MATH_ML = "MATH_ML";
    private static final String CODE = "CODE";
  }
}


package ink.o.w.o.resource.core.symbols.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import ink.o.w.o.resource.core.dot.domain.Dot;
import ink.o.w.o.resource.core.dot.domain.dto.DotElement;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.Set;


/**
 * InkSpaceDocument
 *
 * @author symbols@dingtalk.com
 * @date 2020/03/26
 * @since 1.0.0
 */
@Data

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SymbolsSpaceDocument {
  private String id;
  private DotElement element = generateRootElement();

  public static Set<Dot> toDotSet(DotElement element) {
    Set<Dot> dots = new HashSet<>();

    if (element.getAttributes() != null) {
      dots.add(element.getAttributes());
    }

    return traverse(dots, element);
  }

  public static Set<Dot> traverse(Set<Dot> dots, DotElement element) {
    element.getChildren().forEach(v -> {
      dots.add(v.getAttributes());
      if (v.getChildren().size() > 0) {
        traverse(dots, v);
      }
    });

    return dots;
  }

  public static DotElement generateRootElement() {
    return new DotElement()
        .setType(DotElement.Type.ROOT)
        .setIndex(0)
        .setPath(ArrayUtils.toArray(0));
  }
}

package ink.o.w.o.resource.core.symbols.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import ink.o.w.o.resource.core.dot.domain.Dot;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.Set;


/**
 * SymbolsDocument
 *
 * @author symbols@dingtalk.com
 * @date 2020/03/26
 * @since 1.0.0
 */
@Data

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SymbolsDocument {
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

  @Data
  public static class DotElement implements Comparable<DotElement> {
    private Type type;
    private Boolean root;

    private Integer index;
    private Integer[] path;

    private Dot attributes;

    @JsonIgnore
    private DotElement parent;
    private Set<DotElement> children;

    public static DotElement generateRootElement() {
      return new DotElement()
          .setType(DotElement.Type.ROOT)
          .setIndex(0)
          .setPath(ArrayUtils.toArray(0))
          .setRoot(true);
    }

    /**
     * 情况
     * - 首级不同
     * - 首级相同
     * - 中间不同
     * - 尾级不同
     * - 都相同（异常情况）
     *
     * @param element -
     * @return -
     */
    @Override
    public int compareTo(DotElement element) {
      if (!this.path[0].equals(element.getPath()[0])) {
        return this.path[0] - element.getPath()[0];
      }

      var pathLength = this.path.length;
      var targetPathLength = element.getPath().length;

      if (pathLength == targetPathLength) {
        for (int i = 0; i < pathLength; i++) {
          if (!this.path[i].equals(element.getPath()[i])) {
            return this.path[i] - element.getPath()[i];
          }
        }

      } else {
        var minPathLength = Integer.min(pathLength, targetPathLength);
        for (int i = 0; i < minPathLength; i++) {
          if (!this.path[i].equals(element.getPath()[i])) {
            return this.path[i] - element.getPath()[i];
          }

          if (i == minPathLength - 1) {
            return pathLength > targetPathLength ? 1 : -1;
          }
        }
      }

      return 0;
    }

    public enum Type {
      NODE(TypeName.NODE),
      NODE__ONE_TO_ONE(TypeName.NODE__ONE_TO_ONE),
      NODE__ONE_TO_MANY(TypeName.NODE__ONE_TO_MANY),
      NODE_ELEMENT(TypeName.NODE_ELEMENT),
      ROOT(TypeName.ROOT);

      /**
       * 类型名称
       *
       * @date 2020/02/12 16:42
       * @since 1.0.0
       */
      @Getter
      private String typeName;

      Type(String typeName) {
        this.typeName = typeName;
      }
    }

    public static class TypeName {
      public static final String NODE = "NODE";
      public static final String NODE__ONE_TO_ONE = "NODE_WITH_ONE_TO_ONE";
      public static final String NODE__ONE_TO_MANY = "NODE_WITH_ONE_TO_MANY";
      public static final String NODE_ELEMENT = "NODE_ELEMENT";
      public static final String ROOT = "ROOT";
    }
  }
}

package ink.o.w.o.resource.ink.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ink.o.w.o.resource.dot.domain.Dot;
import ink.o.w.o.resource.dot.domain.DotSpace;
import ink.o.w.o.resource.ink.constant.InkType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Map;
import java.util.Set;


/**
 * AbstractInk
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")

@MappedSuperclass
public abstract class InkSpace {

  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "ink_space-uuid")
  @GenericGenerator(name = "ink_space-uuid", strategy = "uuid")
  protected String id;

  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  protected InkType type;

  @Transient
  protected Map<String, Set<Dot>> space;

  public enum DocumentElementType {
    NODE,
    NODE_WITH_ONE_TO_ONE,
    NODE_WITH_ONE_TO_MANY,
    NODE_ELEMENT,
    ROOT;
  }

  @Data
  public static class Document {
    private DocumentElement<?> origin;
  }

  @Data
  public static class DocumentElement<T extends DotSpace> {
    private T dot;
    private DocumentElementType type;
    private Set<DocumentElement<?>> next;
    private DocumentElement<?> last;
  }
}

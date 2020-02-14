package ink.o.w.o.resource.ink.domain;

import ink.o.w.o.resource.ink.constant.InkType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * 墨水
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "t_ink")
public class Ink {
  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
  private Long id;

  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Enumerated(value = EnumType.STRING)
  private InkType type;

  @Lob
  private String content;
}

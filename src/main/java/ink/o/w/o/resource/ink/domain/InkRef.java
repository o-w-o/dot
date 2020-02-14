package ink.o.w.o.resource.ink.domain;

import ink.o.w.o.resource.ink.constant.InkRefMode;
import lombok.Data;

import javax.persistence.*;


/**
 * InkRef
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 15:41
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "t_ink_ref")
public class InkRef {
  @Id
  private Long id;

  @ManyToOne
  private Ink ink;

  @Enumerated(value = EnumType.STRING)
  private InkRefMode mode;
}

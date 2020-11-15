package o.w.o.server.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;


/**
 * EntityManualIdentity
 *
 * @author symbols@dingtalk.com
 * @date /08/19
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data

@MappedSuperclass
public class EntityManualIdentity {
  /**
   * id
   *
   * @date 2020/08/19
   * @since 1.0.0
   */
  @Id
  protected String id;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  protected Date cTime;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  protected Date uTime;

  @PrePersist
  public void recordCreateTime() {
    if (Optional.ofNullable(cTime).isEmpty()) {
      cTime = new Date();
      uTime = new Date();
    }
  }

  @PreUpdate
  public void recordUpdateTime() {
    uTime = new Date();
  }
}

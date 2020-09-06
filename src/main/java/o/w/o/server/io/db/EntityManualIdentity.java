package o.w.o.server.io.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter

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

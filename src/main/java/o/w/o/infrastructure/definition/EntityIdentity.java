package o.w.o.infrastructure.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;


/**
 * EntityWithSpaceDTO
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data

@MappedSuperclass
public class EntityIdentity {
  /**
   * id
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @Id
  @GeneratedValue(generator = "entity-uuid")
  @GenericGenerator(name = "entity-uuid", strategy = "uuid")
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

package o.w.o.server.io.db;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;


/**
 * EntityWithSpace
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@NoArgsConstructor
@Data

@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})

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
  private Date cTime;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date uTime;

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

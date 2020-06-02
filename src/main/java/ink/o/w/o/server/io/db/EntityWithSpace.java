package ink.o.w.o.server.io.db;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.Valid;
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
public class EntityWithSpace<EntityType, EntitySpace> {
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
  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @ManyToOne
  protected EntityType type;
  @Column(unique = true)
  protected String spaceId;
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  protected Object spaceContent;
  @Valid
  @Transient
  @JsonTypedSpace
  private EntitySpace space;
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

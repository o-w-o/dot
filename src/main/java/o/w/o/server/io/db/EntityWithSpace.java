package o.w.o.server.io.db;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import o.w.o.server.io.json.annotation.JsonTypeTargetType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * EntityWithSpace
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter

@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})

@MappedSuperclass
public class EntityWithSpace<EntityType, EntitySpace> extends EntityManualIdentity {
  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @NotNull
  @ManyToOne
  protected EntityType type;

  @Column(unique = true)
  protected String spaceId;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  protected Object spaceContent;

  @Valid
  @Transient
  @JsonTypeTargetType
  protected EntitySpace space;
}

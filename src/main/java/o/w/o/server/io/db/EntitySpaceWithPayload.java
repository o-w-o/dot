package o.w.o.server.io.db;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import o.w.o.server.io.json.annotation.JsonTypeTargetPayloadType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * EntitySpaceWithPayload
 *
 * @author symbols@dingtalk.com
 * @date 2020/08/08
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
public class EntitySpaceWithPayload<EntityType, EntitySpacePayload, EntitySpacePayloadType> extends EntityIdentity {
  /**
   * 类型
   *
   * @date 2020/02/12 12:36
   * @since 1.0.0
   */
  @NotNull
  @ManyToOne
  protected EntitySpacePayloadType payloadType;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  protected Object payloadContent;

  @Lob
  protected String payloadText;

  @Valid
  @Transient
  @JsonTypeTargetPayloadType
  protected EntitySpacePayload payload;

  @Transient
  protected EntityType type;
}

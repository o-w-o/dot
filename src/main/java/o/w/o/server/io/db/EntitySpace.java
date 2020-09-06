package o.w.o.server.io.db;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;


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
public class EntitySpace<EntityType> extends EntityIdentity {
  @Transient
  protected EntityType type;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  protected Object payload;
}

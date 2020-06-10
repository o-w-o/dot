package ink.o.w.o.resource.core.way.domain;

import ink.o.w.o.server.io.db.EntitySpace;
import ink.o.w.o.server.io.json.annotation.JsonEntityProperty;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import ink.o.w.o.server.validator.SchemaVersion;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Map;


/**
 * AbstractInk
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@Getter
@Setter

@JsonEntityProperty
@JsonTypedSpace

@MappedSuperclass
public abstract class WaySpace extends EntitySpace<WayType.TypeEnum> {
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Object schema;

  @SchemaVersion
  private String version;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> schemata;
}

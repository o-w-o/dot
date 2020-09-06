package o.w.o.resource.symbols.field.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import o.w.o.server.io.db.EntitySpaceWithPayload;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import o.w.o.server.io.json.annotation.JsonTypeTargetType;

import javax.persistence.MappedSuperclass;


/**
 * FieldSpace
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:00
 * @since 1.0.0
 */

@NoArgsConstructor
@Getter
@Setter

@JsonEntityProperty
@JsonTypeTargetType

@MappedSuperclass
public abstract class FieldSpace<P, PT> extends EntitySpaceWithPayload<FieldType.TypeEnum, P, PT> {
}

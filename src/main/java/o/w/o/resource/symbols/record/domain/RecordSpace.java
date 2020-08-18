package o.w.o.resource.symbols.record.domain;

import lombok.Getter;
import lombok.Setter;
import o.w.o.server.io.db.EntitySpaceWithPayload;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import o.w.o.server.io.json.annotation.JsonTypeTargetType;

import javax.persistence.MappedSuperclass;


/**
 * RecordSpace
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 12:36
 * @since 1.0.0
 */
@Getter
@Setter

@JsonEntityProperty
@JsonTypeTargetType

@MappedSuperclass
public abstract class RecordSpace<P, PT> extends EntitySpaceWithPayload<RecordType.TypeEnum, P, PT> {
}

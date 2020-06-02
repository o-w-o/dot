package ink.o.w.o.resource.core.dot.domain;


import ink.o.w.o.server.io.db.EntitySpace;
import ink.o.w.o.server.io.json.annotation.JsonEntityProperty;
import ink.o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * DotSpace
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:00
 * @since 1.0.0
 */

@NoArgsConstructor
@Getter
@Setter

@JsonEntityProperty
@JsonTypedSpace

@MappedSuperclass
public abstract class DotSpace extends EntitySpace<DotType.DotTypeEnum> {
}

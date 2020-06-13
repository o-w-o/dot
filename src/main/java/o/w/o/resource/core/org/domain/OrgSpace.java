package o.w.o.resource.core.org.domain;


import o.w.o.server.io.db.EntitySpace;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import o.w.o.server.io.json.annotation.JsonTypedSpace;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * AbstractInkUnit
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
public abstract class OrgSpace extends EntitySpace<OrgType.TypeEnum> {
}

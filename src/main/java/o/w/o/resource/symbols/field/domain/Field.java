package o.w.o.resource.symbols.field.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import o.w.o.server.io.db.EntityWithSpace;
import o.w.o.server.io.json.annotation.JsonEntityProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Field
 *
 * @author symbols@dingtalk.com
 * @date 2020/1/1
 * @since 1.0.0
 */

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

@JsonEntityProperty

@Entity
@Table(name = "t_sym_field")
public class Field extends EntityWithSpace<FieldType, FieldSpace> {
}

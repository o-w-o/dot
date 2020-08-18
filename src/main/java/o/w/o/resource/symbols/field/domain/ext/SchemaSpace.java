package o.w.o.resource.symbols.field.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.resource.symbols.field.domain.FieldType;
import o.w.o.server.io.json.annotation.JsonEntityProperty;
import o.w.o.server.io.json.annotation.JsonTypeTargetType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 架构单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:03
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(FieldType.TypeName.SCHEMA)
@JsonEntityProperty
@JsonTypeTargetType

@Entity
@Table(name = "t_s_field__schema")
public class SchemaSpace extends FieldSpace<TextSpacePayload, TextSpacePayloadType> {
  @Override
  public FieldType.TypeEnum getType() {
    return FieldType.TypeEnum.SCHEMA;
  }
}


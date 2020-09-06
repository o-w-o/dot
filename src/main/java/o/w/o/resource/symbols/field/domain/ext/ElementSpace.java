package o.w.o.resource.symbols.field.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.resource.symbols.field.domain.FieldType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 元素单元
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:03
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(FieldType.TypeName.TEXT)

@Entity
@Table(name = "t_sym_field__text")
public class ElementSpace extends FieldSpace<ElementSpacePayload, ElementSpacePayloadType> {
  @Override
  public FieldType.TypeEnum getType() {
    return FieldType.TypeEnum.ELEMENT;
  }
}


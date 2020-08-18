package o.w.o.resource.symbols.record.domain.ext;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import o.w.o.resource.symbols.record.domain.RecordSpace;
import o.w.o.resource.symbols.record.domain.RecordType;
import o.w.o.server.io.json.annotation.JsonEntityProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 客观记录体
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/12 17:03
 * @since 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@JsonTypeName(RecordType.TypeName.OBJECT)
@JsonEntityProperty

@Entity
@Table(name = "t_s_record_object")
public class ObjectSpace extends RecordSpace<ObjectSpacePayload, ObjectSpacePayloadType> {
  @Override
  public RecordType.TypeEnum getType() {
    return RecordType.TypeEnum.OBJECT;
  }
}


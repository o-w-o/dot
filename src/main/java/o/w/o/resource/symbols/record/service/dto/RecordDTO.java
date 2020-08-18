package o.w.o.resource.symbols.record.service.dto;

import lombok.Getter;
import lombok.Setter;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.record.domain.RecordSpace;

import java.util.Set;

@Getter
@Setter
public class RecordDTO<T extends RecordSpace> extends RecordSpace {
  private Set<Field> modifiedFields;
}

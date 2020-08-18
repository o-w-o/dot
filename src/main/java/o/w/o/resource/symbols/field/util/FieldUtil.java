package o.w.o.resource.symbols.field.util;

import com.google.common.collect.Sets;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.FieldType;

import java.util.EnumMap;
import java.util.Set;

public class FieldUtil {

  public static EnumMap<FieldType.TypeEnum, Set<Field>> groupFieldsByType(Set<Field> fields) {
    var map = new EnumMap<FieldType.TypeEnum, Set<Field>>(FieldType.TypeEnum.class);
    fields.forEach(field -> {
      var type = field.getType().getType();
      if (map.containsKey(type)) {
        map.get(type).add(field);
      } else {
        map.put(type, Sets.newHashSet(field));
      }
    });
    return map;
  }

}

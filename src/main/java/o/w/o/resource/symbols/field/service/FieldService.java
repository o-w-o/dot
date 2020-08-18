package o.w.o.resource.symbols.field.service;

import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.server.io.service.ServiceResult;

import java.util.List;
import java.util.Set;

public interface FieldService {
  ServiceResult<Set<Field>> preprocessFields(Set<Field> fields);

  ServiceResult<Set<Field>> postprocessFields(Set<Field> fields);

  ServiceResult<Field> save(Field field);

  ServiceResult<Field> persist(ResourceSpace resourceSpace);

  ServiceResult<Field> fetch(String fieldId);

  ServiceResult<List<Field>> fetch(String[] fieldIds);

  ServiceResult<Boolean> delete(Field field);

  interface FieldStoreService {
    ServiceResult<ResourceSpace> storeTemporarily(ResourceSpace resourceSpace);

    ServiceResult<ResourceSpace> storePermanently(ResourceSpace resourceSpace);

    ServiceResult<Boolean> move(ResourceSpace resourceSpace);

    ServiceResult<Boolean> changeVisible(ResourceSpace resourceSpace);

    ServiceResult<Boolean> copy(ResourceSpace resourceSpace);

    ServiceResult<Boolean> remove(ResourceSpace resourceSpace);
  }
}

package o.w.o.resource.symbols.field.service;

import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface FieldService {
  ServiceResult<Set<Field>> preprocessFields(Set<Field> fields);

  ServiceResult<Set<Field>> postprocessFields(Set<Field> fields);

  ServiceResult<Page<Field>> retrieveFields(Example<Field> field, Pageable page);

  ServiceResult<List<Field>> listMyResourceByDir(String dirPath);

  ServiceResult<Field> save(Field field);

  ServiceResult<Field> persist(ResourceSpace resourceSpace);

  ServiceResult<Field> fetch(String fieldId);

  ServiceResult<List<Field>> fetch(String[] fieldIds);

  ServiceResult<Boolean> delete(Field field);

  interface FieldStoreService {
    ServiceResult<ResourceSpace> stage(ResourceSpace resourceSpace);

    ServiceResult<ResourceSpace> persist(ResourceSpace resourceSpace);

    ServiceResult<Boolean> move(ResourceSpace resourceSpace);

    ServiceResult<Boolean> changeVisible(ResourceSpace resourceSpace);

    ServiceResult<Boolean> copy(ResourceSpace resourceSpace);

    ServiceResult<Boolean> remove(ResourceSpace resourceSpace);
  }
}

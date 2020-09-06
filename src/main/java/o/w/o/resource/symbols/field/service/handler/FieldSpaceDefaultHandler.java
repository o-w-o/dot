package o.w.o.resource.symbols.field.service.handler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.resource.symbols.field.repository.FieldRepository;
import o.w.o.resource.symbols.field.repository.FieldSpaceRepository;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Component;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
public class FieldSpaceDefaultHandler<T extends FieldSpace> extends FieldSpaceHandler {
  private FieldRepository fieldRepository;
  private FieldSpaceRepository<T> fieldSpaceRepository;

  @Override
  public ServiceResult<Field> fetch(String fieldId) {
    return ServiceResult.success(
        fieldRepository.findById(fieldId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Field -> id[ %s ]", fieldId))
        )
    );
  }

  @Override
  public ServiceResult<FieldSpace> fetchSpace(String fieldSpaceId) {
    return ServiceResult.success(
        fieldSpaceRepository.findById(fieldSpaceId).orElseThrow(
            () -> new ServiceException(String.format("未找到 Field -> id[ %s ]", fieldSpaceId))
        )
    );
  }

  @SuppressWarnings({"unchecked", ""})
  public ServiceResult<Field> create(Field field) {
    T space = (T) field.getSpace();

    T createdFieldSpace = fieldSpaceRepository.save(space);
    var spaceMountedField = field
        .setSpace(createdFieldSpace)
        .setSpaceId(createdFieldSpace.getId());

    spaceMountedField.setSpaceContent(createdFieldSpace);
    spaceMountedField.setId(createdFieldSpace.getId());
    logger.info("spaceMountedField -> [{}]", spaceMountedField);

    return ServiceResult.success((Field) spaceMountedField);
  }

  @Override
  public ServiceResult<Field> persist(Field field) {
    throw ServiceException.of("未实现的方法！");
  }

  @Override
  public ServiceResult<Field> process(Field field) {
    throw ServiceException.unsupport();
  }

  @Override
  public ServiceResult<Set<Field>> process(Set<Field> field) {
    throw ServiceException.unsupport();
  }

  @Override
  public ServiceResult<Boolean> destory(FieldSpace space) {
    throw ServiceException.unsupport();
  }
}

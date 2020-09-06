package o.w.o.resource.symbols.field.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.resource.symbols.field.domain.FieldType;
import o.w.o.resource.symbols.field.domain.ext.ElementSpace;
import o.w.o.resource.symbols.field.repository.FieldSpaceRepository;
import o.w.o.resource.symbols.field.service.handler.FieldSpaceDefaultHandler;
import o.w.o.resource.symbols.field.service.handler.FieldSpaceHandler;
import o.w.o.resource.symbols.field.service.handler.FieldSpaceHandlerFactory;
import o.w.o.resource.symbols.field.service.handler.FieldTypeSelector;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

@Slf4j
@Component
@FieldTypeSelector(FieldType.TypeEnum.ELEMENT)
public class ElementFieldSpaceHandler extends FieldSpaceHandler {

  @Resource
  FieldSpaceRepository<ElementSpace> textSpaceFieldSpaceRepository;

  @Resource
  FieldSpaceHandlerFactory<ElementSpace> fieldSpaceHandlerFactory;
  FieldSpaceDefaultHandler<ElementSpace> fieldSpaceDefaultHandler;

  @Resource
  private ElementFieldSpacePayloadHandlerHolder elementFieldSpacePayloadHandlerHolder;

  @PostConstruct
  private void init() {
    fieldSpaceDefaultHandler = fieldSpaceHandlerFactory.getDefaultHandler(textSpaceFieldSpaceRepository);
  }

  @Override
  public ServiceResult<Field> fetch(String fieldId) {
    return fieldSpaceDefaultHandler.fetch(fieldId);
  }

  @Override
  public ServiceResult<FieldSpace> fetchSpace(String fieldSpaceId) {
    return fieldSpaceDefaultHandler.fetchSpace(fieldSpaceId);
  }

  @Override
  public ServiceResult<Boolean> destory(FieldSpace space) {
    return fieldSpaceDefaultHandler.destory(space);
  }

  @Override
  public ServiceResult<Field> persist(Field field) {
    throw ServiceException.of("不支持的方法！当前类型不适用，仅用于 Resource！");
  }

  @Override
  public ServiceResult<Field> process(Field field) {
    var space = (ElementSpace) field.getSpace();
    logger.info("field -> [{}], payload -> [{}]", space, space.getPayload());

    elementFieldSpacePayloadHandlerHolder.select(space.getPayloadType().getType()).ifPresent(handler -> {
      var payload = handler.process(space).guard();
      space.setPayload(payload);
      space.setPayloadContent(payload);
    });

    var createdFieldSpace = textSpaceFieldSpaceRepository.save(space);
    var spaceMountedField = field
        .setSpace(createdFieldSpace)
        .setSpaceId(createdFieldSpace.getId());

    spaceMountedField.setSpaceContent(createdFieldSpace);
    logger.info("spaceMountedField -> [{}]", spaceMountedField);

    return ServiceResult.success((Field) spaceMountedField);
  }

  @Override
  public ServiceResult<Set<Field>> process(Set<Field> field) {
    throw ServiceException.unsupport();
  }
}

package o.w.o.resource.symbols.field.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.FieldSpace;
import o.w.o.resource.symbols.field.domain.FieldType;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.resource.symbols.field.repository.FieldSpaceRepository;
import o.w.o.resource.symbols.field.service.FieldService;
import o.w.o.resource.symbols.field.service.handler.FieldSpaceDefaultHandler;
import o.w.o.resource.symbols.field.service.handler.FieldSpaceHandler;
import o.w.o.resource.symbols.field.service.handler.FieldSpaceHandlerFactory;
import o.w.o.resource.symbols.field.service.handler.FieldTypeSelector;
import o.w.o.resource.symbols.field.util.FieldUtil;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

@Slf4j
@Component
@FieldTypeSelector(FieldType.TypeEnum.RESOURCE)
public class ResourceFieldSpaceHandler extends FieldSpaceHandler {
  @Resource
  private FieldSpaceRepository<ResourceSpace> fieldSpaceRepository;

  @Resource
  private FieldSpaceHandlerFactory<ResourceSpace> fieldSpaceHandlerFactory;
  private FieldSpaceDefaultHandler<ResourceSpace> fieldSpaceDefaultHandler;

  @Resource
  private ResourceFieldSpacePayloadHandlerHolder resourceFieldSpacePayloadHandlerHolder;

  @Resource
  private FieldService.FieldStoreService fieldStoreService;

  @PostConstruct
  private void init() {
    fieldSpaceDefaultHandler = fieldSpaceHandlerFactory.getDefaultHandler(fieldSpaceRepository);
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
  public ServiceResult<Field> process(Field field) {
    throw ServiceException.unsupport();
  }

  @Override
  public ServiceResult<Field> persist(Field field) {
    var space = (ResourceSpace) field.getSpace();
    logger.info("field -> [{}], payload -> [{}]", space, space.getPayload());

    var processedResource = processPersisted(
        processStaged(
            processStored(space)
        )
    );

    var createdFieldSpace = fieldSpaceRepository.save((ResourceSpace) processedResource);
    var spaceMountedField = field
        .setSpace(createdFieldSpace)
        .setSpaceId(createdFieldSpace.getId());

    spaceMountedField.setSpaceContent(createdFieldSpace);
    logger.info("spaceMountedField[{}] -> [{}]", space.getStage(), spaceMountedField);

    return ServiceResult.success(
        fieldSpaceDefaultHandler.create((Field) spaceMountedField).guard()
    );

  }

  private ResourceSpace processStored(ResourceSpace space) {
    logger.info("field -> [{}], payload -> [{}]", space, space.getPayload());

    fieldSpaceRepository
        .findOne(Example.of(new ResourceSpace().setDir(space.getDir()).setName(space.getName())))
        .ifPresent((v) -> {
          throw ServiceException.of(
              String.format("相同路径，已存在同名资源！ [%s]", v.getDir())
          );
        });

    return fieldStoreService.storeTemporarily(
        space.setStage(ResourceSpace.Stage.STAGING)
    ).guard();
  }

  private ResourceSpace processStaged(ResourceSpace space) {
    logger.info("field -> [{}], payload -> [{}]", space, space.getPayload());

    return fieldStoreService.storePermanently(
        space
            .setVisibility(ResourceSpace.Visibility.PRIVATE)
            .setStage(ResourceSpace.Stage.PERSISTING)
    ).guard();
  }

  private ResourceSpace processPersisted(ResourceSpace space) {
    logger.info("field -> [{}], payload -> [{}]", space, space.getPayload());
    var type = space.getPayloadType();

    resourceFieldSpacePayloadHandlerHolder.select(type.getType()).ifPresent(handler -> {
      space.setPayloadContent(handler.process(space).guard());
    });

    return space;
  }

  @Override
  public ServiceResult<Set<Field>> process(Set<Field> fields) {
    var fieldsByStage = FieldUtil.groupFieldsByType(fields);
    throw ServiceException.unsupport();
  }
}

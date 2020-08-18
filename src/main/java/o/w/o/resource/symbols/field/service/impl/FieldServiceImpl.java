package o.w.o.resource.symbols.field.service.impl;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.field.domain.Field;
import o.w.o.resource.symbols.field.domain.FieldType;
import o.w.o.resource.symbols.field.domain.ext.ResourceSpace;
import o.w.o.resource.symbols.field.repository.FieldRepository;
import o.w.o.resource.symbols.field.service.FieldService;
import o.w.o.resource.symbols.field.service.handler.FieldSpaceHandlerHolder;
import o.w.o.resource.symbols.field.util.FieldUtil;
import o.w.o.resource.symbols.field.util.ResourceFieldSpaceUtil;
import o.w.o.server.io.service.ServiceException;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * FieldServiceImpl
 *
 * @author symbols@dingtalk.com
 * @date 2020/02/13
 * @since 1.0.0
 */
@Slf4j
@Service
@Transactional(rollbackOn = ServiceException.class)
public class FieldServiceImpl implements FieldService {
  private final FieldSpaceHandlerHolder fieldSpaceHandlerHolder;
  private final FieldRepository fieldRepository;

  @Autowired
  FieldServiceImpl(FieldSpaceHandlerHolder fieldSpaceHandlerHolder, FieldRepository fieldRepository) {
    this.fieldSpaceHandlerHolder = fieldSpaceHandlerHolder;
    this.fieldRepository = fieldRepository;
  }

  @Override
  public ServiceResult<Field> fetch(String fieldId) {
    logger.info("处理 FIELD 开始：{}", fieldId);

    var record = fieldRepository.findById(fieldId).orElseThrow(() -> new ServiceException(String.format("Record id[ %s ] 不存在！", fieldId)));
    var status = new AtomicReference<>(false);
    var result = new AtomicReference<Field>(null);

    fieldSpaceHandlerHolder.select(record.getType().getType()).ifPresent(handler -> {
      result.set(handler.fetch(record.getId()).guard());
      status.set(true);
    });

    logger.info("处理 FIELD 结束：{}", status.get());
    return ServiceResult.success(result.get());
  }

  @Override
  public ServiceResult<List<Field>> fetch(String[] fieldIds) {
    return null;
  }

  @Override
  public ServiceResult<Field> save(Field field) {
    logger.info("处理 FIELD 开始： type -> [ {} ]", field.getType());

    var result = new AtomicReference<ServiceResult<Field>>(null);

    fieldSpaceHandlerHolder.select(field.getType().getType()).ifPresent(handler -> result.set(handler.process(field)));

    var spaceMountedRecord = result.get().guard();
    var createdRecord = fieldRepository.save(spaceMountedRecord);

    logger.info("处理 FIELD 结束： success -> [ {} ]", result.get().getSuccess());
    return ServiceResult.success(createdRecord);
  }

  @Override
  public ServiceResult<Field> persist(ResourceSpace resourceSpace) {
    var field = new Field();

    var payload = ResourceFieldSpaceUtil.generateResourceSpacePayload(resourceSpace);
    resourceSpace.setPayload(payload);
    resourceSpace.setPayloadContent(payload);
    resourceSpace.setPayloadType(payload.getPayloadType());

    field
        .setType(FieldType.of(FieldType.TypeEnum.RESOURCE))
        .setSpace(resourceSpace);

    var res = new AtomicReference<Field>(null);
    fieldSpaceHandlerHolder.select(FieldType.TypeEnum.RESOURCE).ifPresent(
        fieldSpaceHandler -> {
          res.set(fieldSpaceHandler.process(field).guard());
        }
    );

    return ServiceResult.success(fieldRepository.save(res.get()));
  }

  @Override
  public ServiceResult<Boolean> delete(Field field) {
    fieldSpaceHandlerHolder.select(field.getType().getType()).ifPresent(fieldSpaceHandler -> {
      if (fieldSpaceHandler.destory(field.getSpace()).guard()) {
        fieldRepository.delete(field);
      }
    });
    return ServiceResult.success(true);
  }

  @Override
  public ServiceResult<Set<Field>> preprocessFields(Set<Field> fields) {
    var result = new AtomicReference<ServiceResult<Set<Field>>>(
        ServiceResult.success(new HashSet<>())
    );

    var groupFields = FieldUtil.groupFieldsByType(fields);

    groupFields.forEach((k, v) -> {
      if (!v.isEmpty()) {
        fieldSpaceHandlerHolder
            .select(k)
            .ifPresent(
                fieldSpaceHandler -> {
                  var payload = result.get().getPayload();

                  if (payload.addAll(fieldSpaceHandler.process(v).guard())) {
                    result.get().setPayload(payload);
                  } else {
                    logger.warn("Field[preprocessFields] payload.addAll fail ！");
                  }
                }
            );
      }
    });

    return result.get();
  }

  @Override
  public ServiceResult<Set<Field>> postprocessFields(Set<Field> fields) {
    return ServiceResult.success(fields);
  }
}

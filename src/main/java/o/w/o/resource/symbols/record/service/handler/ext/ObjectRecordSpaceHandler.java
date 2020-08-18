package o.w.o.resource.symbols.record.service.handler.ext;

import lombok.extern.slf4j.Slf4j;
import o.w.o.resource.symbols.record.domain.Record;
import o.w.o.resource.symbols.record.domain.RecordSpace;
import o.w.o.resource.symbols.record.domain.RecordType;
import o.w.o.resource.symbols.record.domain.ext.ObjectSpace;
import o.w.o.resource.symbols.record.domain.ext.ObjectSpacePayload;
import o.w.o.resource.symbols.record.repository.RecordSpaceRepository;
import o.w.o.resource.symbols.record.service.handler.RecordSpaceDefaultHandler;
import o.w.o.resource.symbols.record.service.handler.RecordSpaceHandler;
import o.w.o.resource.symbols.record.service.handler.RecordSpaceHandlerFactory;
import o.w.o.resource.symbols.record.service.handler.RecordTypeSelector;
import o.w.o.server.io.service.ServiceResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
@RecordTypeSelector(value = RecordType.TypeEnum.OBJECT)
public class ObjectRecordSpaceHandler extends RecordSpaceHandler {
  @Resource
  private RecordSpaceRepository<ObjectSpace> recordSpaceRepository;

  @Resource
  private RecordSpaceHandlerFactory recordSpaceHandlerFactory;
  private RecordSpaceDefaultHandler<RecordSpace> recordSpaceDefaultHandler;

  @PostConstruct
  private void init() {
    recordSpaceDefaultHandler = recordSpaceHandlerFactory.getDefaultHandler(recordSpaceRepository);
  }

  @Override
  public ServiceResult<Record> create(Record record) {
    record.setFields(this.recordSpaceDefaultHandler.preprocessFields(record.getFields()).guard());

    var space = (ObjectSpace) record.getSpace();
    logger.info("record -> [{}], payload -> [{}]", space, space.getPayload());

    var type = space.getPayload().getPayloadType();
    space.setPayloadType(type);
    switch (type.getType()) {
      case DIARY -> {
        var payload = (ObjectSpacePayload.Diary) space.getPayload();
        space.setPayloadContent(payload);
      }
      default -> {
        logger.warn("未支持的 ObjectSpacePayload 类型 [{}]", type.getType().getTypeName());
      }
    }

    var createdRecordSpace = recordSpaceRepository.save((ObjectSpace) record.getSpace());
    var spaceMountedRecord = (Record) record
        .setSpace(createdRecordSpace)
        .setSpaceId(createdRecordSpace.getId())
        .setSpaceContent(createdRecordSpace);

    spaceMountedRecord
        .setFields(this.recordSpaceDefaultHandler.postprocessFields(spaceMountedRecord.getFields()).guard());
    logger.info("spaceMountedRecord -> [{}]", spaceMountedRecord);

    return ServiceResult.success(spaceMountedRecord);
  }

  @Override
  public ServiceResult<Record> fetch(String recordId, RecordType recordType) {
    return recordSpaceDefaultHandler.fetch(recordId, recordType);
  }

  @Override
  public ServiceResult<RecordSpace> fetchSpace(String recordSpaceId, RecordType recordType) {
    return recordSpaceDefaultHandler.fetchSpace(recordSpaceId, recordType);
  }
}
